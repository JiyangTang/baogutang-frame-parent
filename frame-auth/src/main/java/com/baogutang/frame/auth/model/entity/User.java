package com.baogutang.frame.auth.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Token携带信息
 *
 * @author N1KO
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class User {

    private Long userId;

    private String mobile;

    private String name;

}
