package com.test.pub.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    private int id;
    private int age;
    private int score;
    private String name;
    private Integer ageRank;
    private Integer scoreRank;

}
