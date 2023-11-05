package com.example.study.interfaces;

import com.example.study.model.network.Header;

public interface CrudInterface<Req, Res> {

    Header<Res> create(Req request);   //todo request object 추가

    Header<Res> read(Long id);

    Header<Res> update(Req request);

    Header<Res> delete(Long id);
}
