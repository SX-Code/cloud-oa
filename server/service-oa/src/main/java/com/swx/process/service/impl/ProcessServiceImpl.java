package com.swx.process.service.impl;

import com.swx.model.process.Process;
import com.swx.process.mapper.ProcessMapper;
import com.swx.process.service.ProcessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 审批类型 服务实现类
 * </p>
 *
 * @author sw-code
 * @since 2023-04-12
 */
@Service
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process> implements ProcessService {

}
