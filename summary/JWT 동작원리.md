생성한 클래스

| 패키지    | 생성한 클래스               | 목적 |
| --------- | --------------------------- | ---- |
| config    | SecurityConfig              |      |
| jwtConfig | JwtAccessDeniedHandler      |      |
|           | JwtAuthenticationEntryPoint |      |
|           | JwtFilter                   |      |
|           | JwtSecurityConfig           |      |
|           | TokenProvider               |      |



로그인 요청 :  "/api/authenticate"

- 로그인 요청으로 토큰을 생성하여 프론트에 반환
- 해당 요청은 SecurityConfig에 permitAll() 목록에 넣는다.

다른 요청

- 그 외 요청은 JwtFilter를 통과하며, JWT를 가지고 있는지, 유효한지 확인하며, 유효하다면 JWT를 이용하여 authentication을 반환해준다. 
- authentication 은 아래처럼 new usernamePasswordAuthenticationToken을 이용하여 authentication을 반환한다.

```java
public Authentication getAuthentication(String token) {
    Claims claims = Jwts
            .parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();

    Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

    User principal = new User(claims.getSubject(), "", authorities);

    return new UsernamePasswordAuthenticationToken(principal, token, authorities);
```


<br>

### JwtFilter

```java
public class JwtFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private TokenProvider tokenProvider;
    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    /**
     * JWT 의 인증정보를 현재 실행중인 SecurityContext에 저장하는 역할을 수행한다.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        // 헤더에서 토큰을 받아온다.
        String jwt = resolveToken(httpServletRequest);
        // 호출한 요청의 URI를 가져와 유효하지 않은 요청의 URI를 확인하는 용도
        String requestURI = httpServletRequest.getRequestURI();

        // 토큰의 유효성을 확인
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            // 권한 생성
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            // SecurityContextHolder에 권한정보 추가
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
        } else {
            logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        chain.doFilter(request, response);
    }

    /**
     * request의 Header에서 토큰 정보를 가져오는 메서드
     * @param request
     * @return
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

```

<br>

### TokenProvider

```java
/**
 * TokenProvider 에서는
 *  1. 토큰을 생성
 *  2. 토큰으로 권한을 생성
 *  3. 토큰의 유효성을 검증
 *
 *  3가지 작업을 한다.
 */
@Component
public class TokenProvider implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private static final String AUTHORITIES_KEY = "auth";

    private final String secret;
    private final long tokenValidityInMilliseconds;

    private Key key;

    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    /**
     * 토큰을 생성해주는 로직
     */
    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    /**
     * 가져온 토큰으로 인증하는 메서드
     */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);


    }

    /**
     * 토큰의 유효성 검증을 수행하는 ValidationToken 메소드 추가 -> 필터에서 사용
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}

```



