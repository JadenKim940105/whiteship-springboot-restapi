package me.summerbell.whiteshipspringbootrestapi.accounts;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter @Setter @EqualsAndHashCode(of="id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class Account {

    @Id @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String email;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER) // set, list 는 default 가 LAZY
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles;
}
