package online.money_daisuki.api.monkey.basegame.shapes;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.BiConverter;
import online.money_daisuki.api.io.json.JsonMap;

public interface CollisionShapeLoader extends BiConverter<JsonMap, Spatial, CollisionShape> {

}
