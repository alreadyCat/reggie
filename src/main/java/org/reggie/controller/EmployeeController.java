package org.reggie.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.reggie.common.BaseContext;
import org.reggie.common.Res;
import org.reggie.pojo.Employee;
import org.reggie.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Value("${reggie.session-key}")
    private String Session_Key ;
    @Autowired
    EmployeeService employeeService;

    @PostMapping("/login")
    public Res<Employee> login(HttpServletRequest req, @RequestBody Employee emp) {

        String pwd = emp.getPassword();
        pwd = DigestUtils.md5DigestAsHex(pwd.getBytes());

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, emp.getUsername());
        Employee empRes = employeeService.getOne(queryWrapper);

        if (empRes == null || !empRes.getPassword().equals(pwd)) {
            return Res.error("账号或密码错误，请重新登录！");
        }

        if (empRes.getStatus() == 0) {
            return Res.error("账号已禁用，请联系管理员开启账户");
        }

        req.getSession().setAttribute(Session_Key, empRes.getId());


        return Res.success(empRes);
    }

    @PostMapping("/logout")
    public Res<String> logout(HttpServletRequest req) {
        req.getSession().removeAttribute(Session_Key);
        return Res.success("退出成功");
    }

    @PostMapping
    public Res<String> insert(HttpServletRequest request, @RequestBody Employee emp) {
        log.info("新增员工信息: {}", emp.toString());

        // 初始密码123456
        emp.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));


//        emp.setCreateTime(LocalDateTime.now());
//        emp.setUpdateTime(LocalDateTime.now());
//
//        emp.setCreateUser(empId);
//        emp.setUpdateUser(empId);

        employeeService.save(emp);
        return Res.success("新增员工成功");
    }
    @GetMapping("/page")
    public Res<Page> page(int page,int pageSize,String name){
        // 构造分页构造器
        Page pageCon = new Page(page, pageSize);

        // 构造条件构造器
        LambdaQueryWrapper<Employee> employeeQueryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        employeeQueryWrapper.like(!StringUtils.isEmpty(name),Employee::getName,name);
        // 添加排序条件
        employeeQueryWrapper.orderByDesc(Employee::getUpdateTime);

        // 执行
        employeeService.page(pageCon,employeeQueryWrapper);

        return Res.success(pageCon);
    }
    @PutMapping
    public Res<String> update(HttpServletRequest request,@RequestBody Employee emp){
//        emp.setUpdateUser(empId);
//        emp.setUpdateTime(LocalDateTime.now());
        emp.setStatus(emp.getStatus());

        employeeService.updateById(emp);
        return Res.success("修改成功");
    }

    @GetMapping("/{id}")
    public Res<Employee> getEmpById(@PathVariable Long id){
        log.info("查询员工信息id:{}",id);
        Employee emp = employeeService.getById(id);
        return Res.success(emp);
    }
}
