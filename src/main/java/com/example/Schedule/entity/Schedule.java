package com.example.Schedule.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "제목은 필수 입력 값입니다.")
    @Size(max = 10, message = "제목은 최대 10글자까지 입력할 수 있습니다.")
    private String title;

    @NotBlank(message = "일정은 필수 입력 값입니다.")
    @Column(nullable = false, columnDefinition = "longtext")
    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String password;

    public Schedule(User user, String password, String title, String contents) {
        this.title = title;
        this.contents = contents;
        this.user = user;
        this.password = password;
    }

    public Schedule() {
    }
}
