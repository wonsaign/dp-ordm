package com.zeusas.dp.ordm.service;

import java.util.List;

import com.zeusas.dp.ordm.entity.MenuNode;
import com.zeusas.dp.ordm.entity.MenuTree;
import com.zeusas.security.auth.entity.AuthUser;

public interface MenuManager {
	public List<MenuTree> buildMenu(AuthUser user);

	public List<MenuNode> getRootMenu(AuthUser user);

	public List<MenuNode> getSubmenu(AuthUser user, String parentCode);

	void reload();

	public void save(MenuNode menuNode);

	public void update(MenuNode menuNode);

	public MenuNode getMenuDetail(String id);

	public void delete(Integer id);
}
