package cn.wwc.x.infrastructure.dao;

import cn.wwc.x.infrastructure.dao.po.FormFieldPO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface IFormFieldDao {
    int insertBatch(List<FormFieldPO> list);
    List<FormFieldPO> selectByFormId(Long formId);
}
