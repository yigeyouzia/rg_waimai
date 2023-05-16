package com.cyt.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyt.reggie.entity.AddressBook;
import com.cyt.reggie.mapper.AddressBookMapper;
import com.cyt.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @author cyt
 * @version 1.0
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
