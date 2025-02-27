package com.apptware.hrms.employee;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee_skills")
public class EmployeeSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Enumerated(EnumType.STRING)
    private Proficiency proficiency;

    @Enumerated(EnumType.STRING)
    private Skill skill;

    public enum Proficiency{
        PRIMARY,
        SECONDARY
    }

    public enum Skill {
        JAVA,
        PYTHON,
        ANGULAR,
        REACT,
        DEVOPS
    }

}
