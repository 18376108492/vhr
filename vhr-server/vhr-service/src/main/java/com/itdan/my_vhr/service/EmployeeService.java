package com.itdan.my_vhr.service;

import com.itdan.my_vhr.mapper.EmployeeMapper;
import com.itdan.my_vhr.model.Employee;
import com.itdan.my_vhr.model.RespPageBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class EmployeeService {

    private Logger logger=LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private SimpleDateFormat yearSimple;

    @Autowired
    private SimpleDateFormat mouthSimple;

    @Autowired
    private DecimalFormat decimalFormat;

    /**
     * 获取所有员工信息(分页显示)
     * @param page
     * @param size
     * @param employee
     * @param beginDateSize
     * @return
     */
    public RespPageBean getEmployeeByPage(Integer page,
                                          Integer size,
                                          Employee employee,
                                          Date [] beginDateSize){

        logger.info("获取所有员工信息操作(分页显示)");
        if(page!=null && size!=null){
              page=((page-1)*size);
        }
        List<Employee> employees=employeeMapper.getEmployeeByPage(page,size,employee,beginDateSize);
        Long total=employeeMapper.getTotal(employee,beginDateSize);
        RespPageBean respPageBean=new RespPageBean(total,employees);

        logger.info("获取所有员工信息操作成功");
        return respPageBean;
    }


    /**
     * 添加员工
     * @param record
     * @return
     */
    public  Integer addEmployee(Employee record){
        logger.info("添加员工操作");
        if(record==null){
            logger.error("添加员工操作失败参数为空");
            throw new NullPointerException("添加员工操作失败参数为空");
        }
        //计算合同期限
        Date beginContract=record.getBeginContract();
        Date endContract= record.getEndContract();
        double mouth=(Double.parseDouble(yearSimple.format(endContract))- Double.parseDouble(yearSimple.format(beginContract)))*12+
                     (Double.parseDouble(mouthSimple.format(endContract))- Double.parseDouble(mouthSimple.format(beginContract)));
        record.setContractTerm(Double.parseDouble(decimalFormat.format(mouth/12)));
        Integer row= employeeMapper.insertSelective(record);
        logger.info("添加员工操作成功");
        return row;
    }

    /**
     * 获取员工最大工号
     * @return
     */
    public Integer  getmaxWordId(){
        logger.info("获取员工最大工号");
        Integer wordId= employeeMapper.maxWorkID();
        logger.info("获取员工最大工号成功");
        return wordId;
    }

    /**
     * 更新员工信息
     * @return
     */
    public Integer updateEmployee(Employee record){
        logger.info("更新员工信息");
        if(record==null){
            logger.error("更新员工操作失败参数为空");
            throw new NullPointerException("更新员工操作失败参数为空");
        }
        Integer row= employeeMapper.updateByPrimaryKeySelective(record);
        logger.info("更新员工信息成功");
        return row;
    }

    /**
     * 删除员工
     * @param id
     * @return
     */
    public Integer deleteEmployee( Integer id){
        logger.info("删除员工信息");
        if(id==null){
            logger.error("删除员工操作失败参数为空");
            throw new NullPointerException("删除员工操作失败参数为空");
        }
        Integer row= employeeMapper.deleteByPrimaryKey(id);
        logger.info("删除员工信息成功");
        return row;
    }

    /**
     * 将员工集合插入数据库中
     * @param employees
     * @return
     */
    public Integer addEmps(List<Employee> employees) {
        logger.info("将员工集合插入数据库中");
        if(employees==null){
            logger.error("将员工集合插入数据库中参数为空");
            throw new NullPointerException("将员工集合插入数据库中参数为空");
        }
        Integer row= employeeMapper.addEmps(employees);
        logger.info("将员工集合插入数据库中成功");
        return row;
    }
}
