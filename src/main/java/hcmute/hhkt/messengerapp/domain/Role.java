package hcmute.hhkt.messengerapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id")
    private UUID id;

    @Size(min = 1, max = 50)
    @Column(name="name", nullable = false, unique = true)
    private String name;

    @Size(max = 200)
    @Column(name="description")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_authority", joinColumns = @JoinColumn(name = "role_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "authority_id", nullable = false))
    private List<Authority> authorities;
}
