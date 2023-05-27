package com.test.pub.entity;
 
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
 
/**
 * (User)实体类
 *
 * @author liu.gc
 * @since 2023-04-21 10:03:36
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = -86867362098611388L;
     
    private Integer id;
     
    private String username;
     
    private String password;
 
}
