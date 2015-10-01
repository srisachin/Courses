package musicdb.analyzer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonHelper {
	public static String createJSONString(Object obj) {

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setPrettyPrinting();
		Gson gson = gsonBuilder.create();
		String jsonStr = gson.toJson(obj);

		return jsonStr;

	}

}
