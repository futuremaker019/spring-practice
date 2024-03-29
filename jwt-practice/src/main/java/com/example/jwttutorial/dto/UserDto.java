package com.example.jwttutorial.dto;

import com.example.jwttutorial.entity.Authority;
import com.example.jwttutorial.entity.UserAccount;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @NotNull
    @Size(min = 3, max = 50)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Size(min = 3, max = 100)
    private String password;

    @NotNull
    @Size(min = 3, max = 50)
    private String nickname;

    private Set<AuthorityDto> authorityDtos = new LinkedHashSet<>();

    public static UserDto from(UserAccount entity) {
        return UserDto.builder()
                .nickname(entity.getNickname())
                .username(entity.getUsername())
                .authorityDtos(
                        entity.getAuthorities().stream()
                                .map(authority -> AuthorityDto.builder().authorityName(authority.getAuthorityName()).build())
                                .collect(Collectors.toUnmodifiableSet())
                )
                .build();
    }
}
