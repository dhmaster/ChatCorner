package com.dhuer.mallchat.common.user.dao;

import com.dhuer.mallchat.common.user.domain.entity.Room;
import com.dhuer.mallchat.common.user.mapper.RoomMapper;
import com.dhuer.mallchat.common.user.service.IRoomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 房间表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/dhmaster/MallChat.git">Jintao_L</a>
 * @since 2024-08-04
 */
@Service
public class RoomDao extends ServiceImpl<RoomMapper, Room> implements IRoomService {

}
