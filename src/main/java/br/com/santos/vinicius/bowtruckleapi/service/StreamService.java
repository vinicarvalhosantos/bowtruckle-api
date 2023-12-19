package br.com.santos.vinicius.bowtruckleapi.service;

import br.com.santos.vinicius.bowtruckleapi.model.StreamInfoModel;

public interface StreamService {

    StreamInfoModel defineStreamInformations(String streamTitle, String gameId);

    StreamInfoModel updateGameInStreamInformations(String gameId, StreamInfoModel streamInfo);

}
