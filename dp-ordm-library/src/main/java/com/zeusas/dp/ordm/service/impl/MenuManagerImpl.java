package com.zeusas.dp.ordm.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.zeusas.common.service.OnStartApplication;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.MenuNode;
import com.zeusas.dp.ordm.entity.MenuTree;
import com.zeusas.dp.ordm.service.MenuManager;
import com.zeusas.dp.ordm.service.MenuService;
import com.zeusas.security.auth.entity.AuthUser;

@Service("menuManager")
public class MenuManagerImpl extends OnStartApplication implements MenuManager {

	static Logger logger = LoggerFactory.getLogger(MenuManagerImpl.class);
	// 全部菜单Node
	final Map<String, MenuNode> menus = new ConcurrentHashMap<>();
	// 所有的菜单
	final Map<String, MenuTree> menusTree = new ConcurrentHashMap<>();
	// 根菜单
	final List<MenuTree> root = new ArrayList<>();

	@Autowired
	private MenuService menuService;

	public MenuNode get(String mid) throws ServiceException {
		return menus.get(mid);
	}

	@Override
	public List<MenuTree> buildMenu(AuthUser user) {
		final List<MenuTree> menuInit = new ArrayList<>();
		Map<String, MenuTree> myMenu = new HashMap<>();

		myMenu.clear();
		// 获取当前用户的所有菜单
		menus.values().stream()//
				.filter(e -> e.containsRoles(user.getRoles()))//
				.forEach(e -> {
					MenuTree mtree = new MenuTree(e);
					myMenu.put(mtree.key(), mtree);
				});
		// 把集合 遍历成一棵树
		myMenu.values().stream().forEach(e -> {
			MenuNode node = e.getNode();

			String pcode = node.getParentCode();
			if (Strings.isNullOrEmpty(pcode)) {
				menuInit.add(e);
			} else {
				MenuTree pmenu = myMenu.get(pcode);
				pmenu.addChild(e);
			}
		});

		myMenu.values()
				.stream()
				.filter(e -> e.getChildern().size() > 1)
				.forEach(
						e -> {
							List<MenuTree> list = e.getChildern();
							Collections.sort(list,
									Comparator.comparing(MenuTree::seqCode));
						});

		Collections.sort(menuInit, Comparator.comparing(MenuTree::seqCode));
		return menuInit;
	}

	void clear() {
		menus.clear();
		menusTree.clear();
		root.clear();
	}
	
	@Override
	public void reload() {
		clear();
		menuService.findAll().forEach(e -> {
			this.menus.put(e.getCode(), e);
			MenuTree mtree = new MenuTree(e);
			menusTree.put(mtree.key(), mtree);
		});

		menusTree.values().stream().forEach(e -> {
			MenuNode node = e.getNode();

			String pcode = node.getParentCode();
			if (Strings.isNullOrEmpty(pcode)) {
				root.add(e);
			} else {
				MenuTree pmenu = menusTree.get(pcode);
				pmenu.addChild(e);
			}
		});
	}

	@Override
	public void onStartLoad() {
		reload();
	}

	@Override
	public void save(MenuNode menuNode) {
		menuService.save(menuNode);
		reload();
	}

	@Override
	public void update(MenuNode menuNode) {
		menuService.update(menuNode);
		reload();
	}

	@Override
	public List<MenuNode> getRootMenu(AuthUser user) {
		return root.stream().map(e -> e.getNode())
				.filter(e -> e.containsRoles(user.getRoles()))
				.sorted(Comparator.comparing(MenuNode::getCode))
				.collect(Collectors.toList());
	}

	@Override
	public List<MenuNode> getSubmenu(AuthUser user, String parentCode) {
		return menusTree.get(parentCode).getChildern().stream()//
				.map(e -> e.getNode())//
				.filter(e -> e.containsRoles(user.getRoles()))//
				.sorted(Comparator.comparing(MenuNode::getCode))//
				.collect(Collectors.toList());
	}

	@Override
	public MenuNode getMenuDetail(String id) {
		return menus.get(id);
	}

	@Override
	public void delete(Integer id) {
		menuService.delete(id);
		reload();
	}
}
