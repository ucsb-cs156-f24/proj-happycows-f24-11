package edu.ucsb.cs156.happiercows.entities;

import lombok.*;

import javax.persistence.*;


@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity(name = "students")
public class Student {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private Long courseId;
  private String fname;
  private String lname;
  private String studentId;
  private String email;

}
