package online.money_daisuki.api.monkey.basegame.form;

import java.util.HashMap;
import java.util.Map;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;

import online.money_daisuki.api.io.json.DefaultJsonMap;
import online.money_daisuki.api.io.json.JsonElement;
import online.money_daisuki.api.io.json.JsonMap;

public final class FormDataStoreState extends BaseAppState {
	private Map<Long, JsonElement> temporaries;
	private Map<Long, JsonElement> persistent;
	
	@Override
	protected void initialize(final Application app) {
		temporaries = new HashMap<>();
		persistent = new HashMap<>();
	}
	@Override
	protected void cleanup(final Application app) {
		temporaries.clear();
		temporaries = null;
		
		persistent.clear();
		persistent = null;
	}
	
	public boolean has(final long id) {
		//return(data.containsKey(id));
		return(true);
	}
	public JsonMap get(final long id) {
		final DefaultJsonMap ret = new DefaultJsonMap();
		
		final JsonElement temp = temporaries.get(id);
		if(temp != null) {
			ret.put("temporary", temp);
		} else {
			ret.put("temporary", new DefaultJsonMap());
		}
		
		final JsonElement per = persistent.get(id);
		if(per != null) {
			ret.put("persistent", per);
		} else {
			ret.put("persistent", new DefaultJsonMap());
		}
		
		return(ret);
	}
	public void put(final long id, final JsonMap data) {
		if(data.containsKey("temporary")) {
			final JsonElement temp = data.get("temporary");
			if(temp != null) {
				temporaries.put(id, temp);
			} else {
				temporaries.remove(id);
			}
		}
		
		if(data.containsKey("persistent")) {
			final JsonElement per = data.get("persistent");
			if(per != null) {
				persistent.put(id, per);
			} else {
				persistent.remove(id);
			}
		}
	}
	public JsonElement removePersistent(final long id) {
		return(persistent.remove(id));
	}
	public JsonElement removeTemporary(final long id) {
		return(temporaries.remove(id));
	}
	
	public void clearTemporaries() {
		temporaries.clear();
	}
	
	@Override
	protected void onEnable() {
		
	}
	@Override
	protected void onDisable() {
		
	}
}
