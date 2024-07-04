package hcmute.hhkt.messengerapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="authorities")
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id")
    private UUID id;

    @Size(min = 1, max = 50)
    @Column(name="name", nullable = false, unique = true)
    private String name;
}
