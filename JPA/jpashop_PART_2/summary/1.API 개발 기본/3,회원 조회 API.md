#### 회원 조회 API 개발

DTO를 사용하라 (또 강조!)

1. 엔티티를 바로 외부 호출에 응답시 보여주지 말아야할 모든 데이터가 노출될수 있다.

2. 데이터를 아래와 같이 응답하게 되면(List의 형태로 바로 반환시), 데이터의 확장에 용이하지 못하게 된다.

```java
class MemberApiController {
  @GetMapping("/api/v1/members")
  public List<Member> memberV1() {
    return memberService.findMembers();
  }
}
```
```javascript
[
  {
    "name": "new-hello"
  },
  {
    "name": "창수"
  },
  {
    "name": "서우"
  }
]
```

아래와 같이 Result Dto에 담아 데이터를 응답하면 확장성이 더 좋아지게 된다.

```java
class MemberApiController {
    /**
     * 1. 엔티티를 직접 반환하게되면 엔티티의 모든 정보가 외부에 노출되어 보안에 취약해진다.
     * 2. 리스트형태로 바로 반환하게되면 스팩 확장에 어려움이 생긴다.
     */
    @GetMapping("/api/v1/members")
    public List<Member> memberV1() {
        return memberService.findMembers();
    }

    /**
     * data라는 필드를 가진 Result로 한번 씌워서 보내줘야한다.
     * 엔티티의 필드명이 변경되어도 외부로 호출되는 필드명을 동일하게 유지할수 있는 장점도 있다.
     */
    @GetMapping("/api/v2/members")
    public Result memberV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());
        return new Result(collect.size() ,collect);

    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }
}
```

```javascript
{
    "count": 3,
    "data": [
        {
            "name": "new-hello"
        },
        {
            "name": "창수"
        },
        {
            "name": "서우"
        }
    ]
}
```



