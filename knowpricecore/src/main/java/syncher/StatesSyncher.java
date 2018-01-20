package syncher;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import utils.HTTPUtils;
import utils.StringUtils;
import dataobjects.LocationState;

public class StatesSyncher extends BaseSyncher {
	public List<LocationState> getStatesList() {
		List<LocationState> statesList = new ArrayList<LocationState>();
		try {
            String dataFromServer = HTTPUtils.getDataFromServer(BASE_URL + "app/trackr/out/data", "GET", false);
            if (StringUtils.isJSONValid(dataFromServer)) {
            	JSONArray jsonArray = new JSONArray(dataFromServer);
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        LocationState stateInfo = new LocationState();
                        stateInfo.setStateName(jsonObject.getString("state"));
                        stateInfo.setStateCode(jsonObject.getString("scode"));
                        stateInfo.setPetrolPrice(jsonObject.getString("petrol"));
                        stateInfo.setDieselPrice(jsonObject.getString("diesel"));
                        stateInfo.setGasPrice(jsonObject.getString("gas"));
                        statesList.add(stateInfo);
                    }
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
		return statesList;
	}
}
