package br.com.simplesdental.entity.profissional;

import br.com.simplesdental.entity.ResultSet;

import java.util.List;

interface ProfissionalCustomRepository {
    List<ResultSet> findAllByFields(String filter, List<String> fields);
}
