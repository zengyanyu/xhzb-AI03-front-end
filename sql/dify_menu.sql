-- ============================================================
-- Dify 小智 菜单 SQL
-- 功能: 在 "星海智询"(菜单ID 2052) 目录下新增 "dify小智" 菜单
--       页面组件: nursing/dify/index (Dify 智能体聊天: 流式聊天/会话列表/历史消息/删除会话)
-- 说明:
--   1. 不指定 menu_id, 由自增主键自动分配, 避免与已有菜单ID冲突
--   2. 超级管理员(admin)拥有所有菜单权限, 执行后重新登录即可看到新菜单
--   3. 普通角色需在 "系统管理-角色管理" 中勾选该菜单, 或执行下方注释中的授权语句
-- ============================================================

INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES ('dify小智', 2052, 2, 'difyxiaozhi', 'nursing/dify/index', NULL, '', 1, 0, 'C', '0', '0', '', 'message', 'admin', sysdate(), '', NULL, 'Dify智能体聊天菜单');

-- 如需给普通角色(示例 role_id=2)授权, 取消注释并执行(使用 LAST_INSERT_ID 自动取新菜单ID):
-- INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (2, LAST_INSERT_ID());
