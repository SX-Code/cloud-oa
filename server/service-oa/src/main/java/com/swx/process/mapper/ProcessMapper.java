package com.swx.process.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.swx.model.process.Process;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swx.vo.process.ProcessQueryVo;
import com.swx.vo.process.ProcessVo;
import com.swx.vo.system.page.CustomPage;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 审批类型 Mapper 接口
 * </p>
 *
 * @author sw-code
 * @since 2023-04-12
 */
public interface ProcessMapper extends BaseMapper<Process> {

    IPage<Process> selectPageVo(CustomPage<ProcessVo> pageParam, @Param("vo") ProcessQueryVo processQueryVo);
}
