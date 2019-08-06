package com.chuhongtao.dao;

import com.chuhongtao.entity.MenuInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuDao extends JpaRepository<MenuInfo,Long> {
    @Query(value = "select bm.* from base_role_menu brm INNER JOIN base_menu bm ON brm.menuId=bm.id where brm.roleId=?1 and bm.leval=?2 ",nativeQuery = true)
    public List<MenuInfo> getFirstMenuInfo(Long roleId, Integer leval);
}
