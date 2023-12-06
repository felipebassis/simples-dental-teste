package br.com.simplesdental.entity.contato;

import br.com.simplesdental.entity.ResultSet;

import java.util.List;

interface ContatoCustomRepository {
    List<ResultSet> findAllByFields(String filter, List<String> fields);
}
