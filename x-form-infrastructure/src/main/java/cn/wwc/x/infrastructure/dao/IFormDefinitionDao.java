package cn.wwc.x.infrastructure.dao;

import cn.wwc.x.infrastructure.dao.po.FormDefinitionPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IFormDefinitionDao {
    int insert(FormDefinitionPO po);
    FormDefinitionPO selectByFormKey(String formKey);
    FormDefinitionPO selectById(Long id);
    List<FormDefinitionPO> selectList();
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}
