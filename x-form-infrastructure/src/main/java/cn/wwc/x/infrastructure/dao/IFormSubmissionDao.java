package cn.wwc.x.infrastructure.dao;

import cn.wwc.x.infrastructure.dao.po.FormSubmissionPO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface IFormSubmissionDao {
    int insert(FormSubmissionPO po);
    List<FormSubmissionPO> selectByFormId(Long formId);
    Long countByFormId(Long formId);
}
