package me.summerbell.whiteshipspringbootrestapi.events;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import me.summerbell.whiteshipspringbootrestapi.accounts.Account;
import me.summerbell.whiteshipspringbootrestapi.accounts.AccountSerializer;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "id")
@Entity
public class Event {

    @Id @GeneratedValue
    private Integer id;

    private String name;

    private String description;

    private LocalDateTime beginEnrollmentDateTime;

    private LocalDateTime closeEnrollmentDateTime;

    private LocalDateTime beginEventDateTime;

    private LocalDateTime closeEventDateTime;

    private String location;

    private int basePrice;

    private int maxPrice;

    private int limitOfEnrollment;

    private boolean offline;

    private boolean free;

    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;

    @ManyToOne
    @JsonSerialize(using = AccountSerializer.class)
    private Account manager;

    public void update() {
        if(this.basePrice == 0 && maxPrice == 0){
            this.free = true;
        } else
            this.free = false;

        if( this.location==null || this.location.isBlank()){
            this.offline = false;
        } else{
            this.offline = true;
        }
    }
}
