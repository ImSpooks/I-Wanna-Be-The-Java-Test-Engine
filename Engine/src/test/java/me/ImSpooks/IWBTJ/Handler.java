package me.ImSpooks.IWBTJ;

import me.ImSpooks.IWBTJ.game.mainmenu.LoadingScreen;
import me.ImSpooks.IWBTJ.object.IObject;
import me.ImSpooks.IWBTJ.object.gameobjects.kid.Kid;
import me.ImSpooks.IWBTJ.object.gui.IGui;
import me.ImSpooks.IWBTJ.object.interfaces.GameObject;
import me.ImSpooks.IWBTJ.object.interfaces.KidObject;
import me.ImSpooks.IWBTJ.object.interfaces.KidObjects;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Handler {

	private Main main;
	private Kid kid;

	public Handler(Main Main) {
		this.main = Main;
		/*this.keyInputs = new KeyInputs(this);
		this.main.addKeyListener(keyInputs); // 37, 38, 39, 40 = ArrowKeys*/
	}

	public void loadingScreen(){
		LoadingScreen loadingScreen = new LoadingScreen(this);
		object.add(loadingScreen);
		loadingScreen.initialize();
	}


	public LinkedList<IObject> object = new LinkedList<IObject>();
	
	public void tick() {
        for (int i = 0; i < object.size(); i++){
			IObject tempObject = object.get(i);

			tempObject.tick();
			if (tempObject.getClass().getSimpleName().contains("Kid") && !tempObject.getClass().getSimpleName().contains("Fake")) {
            }
        }
	}

	public void render(Graphics g) {
	    List<IGui> guis = new ArrayList<>();
        List<KidObjects> kidObjects = new ArrayList<>();

		oLoop: for (int i = 0; i < object.size(); i++){
			IObject tempObject = object.get(i);
			if (tempObject instanceof KidObject)
			    continue;

			if (tempObject instanceof GameObject) {
			    GameObject gameObject = (GameObject)tempObject;

			    for (double x : new double[] {gameObject.getX(), gameObject.getX() - gameObject.getBounds().getWitdh()}) {
                    if (x < 0 || x > Main.gameWidth) {
                        continue oLoop;
                    }
                }
                for (double y : new double[] {gameObject.getY(), gameObject.getY() - gameObject.getBounds().getHeight()}) {
                    if (y < 0 || y > Main.gameHeight) {
                        continue oLoop;
                    }
                }
            }
            if (tempObject instanceof KidObjects) {
			    kidObjects.add((KidObjects)tempObject);
                continue;
            }
			else if (tempObject instanceof IGui) {
                guis.add((IGui) tempObject);
                continue;
            }

            tempObject.render(g);
		}

		if (getKid() != null) {
            getKid().render(g);
        }

        kidObjects.forEach(kidObjs -> kidObjs.render(g));
		guis.forEach(iGui -> iGui.render(g));
	}
	
	public void addObject(IObject... object){
	    for (IObject objects : object) {
	        if (objects instanceof KidObject && getKid() != null) {
                System.out.println("Trying to spawn second kid, cancelling.");
                continue;
            }
            objects.initialize();
            this.object.add(objects);
        }
	}
	
	public void removeObject(IObject object){
		if (object == null || !this.object.contains(object)) {
			throw new NullPointerException("Trying to remove a object that doesn't exist");
		}
		object.onRemove();
		this.object.remove(object);
	}
	
	public void addMultipleObjects(int amount, IObject object){
		for (int i = 0; i < amount; i++) this.object.add(object);
	}

	public LinkedList<IObject> getObjects() {
		return object;
	}

    public List<IObject> getObjectsAt(int x, int y) {
        return getObjectInRadius(x, y, 0, 33);
    }

    public List<IObject> getObjectInRadius(int x, int y, int radius, int maxDistance){
        List<IObject> objects = new ArrayList<>();

        int px = x - radius;
        int py = y - radius;
        int sx = x + radius;
        int sy = y + radius;

        for (IObject allObject : getObjects()) {
            if (main.distance(px, allObject.getX(), py, allObject.getY()) > maxDistance + main.distance(px, sx, py, sy))
                continue;

            for (int[] hitbox : allObject.getBounds().getPixels()) {
                if (sx == px && sy == py) {
                    if (allObject.getX() + hitbox[0] == px && allObject.getY() + hitbox[1] == py) {
                        objects.add(allObject);
                    }
                }
                else {
                    for (int xx = px; xx <= sx; xx++) {
                        for (int yy = py; yy <= sy; yy++) {
                            if (allObject.getX() + hitbox[0] == xx && allObject.getY() + hitbox[1] == yy) {
                                objects.add(allObject);
                            }
                        }
                    }
                }
            }
        }

        return objects;
    }

    public List<IObject> getObjectInRadius(IObject object, int radius, int maxDistance){
        List<IObject> objects = new ArrayList<>();

        int width = 0;
        int height = 0;

        if (object instanceof GameObject) {
            if (((GameObject) object).getSprite() != null) {
                width = ((GameObject) object).getSprite().getWidth();
                height = ((GameObject) object).getSprite().getHeight();
            }
        }
        else if (object instanceof Kid) {
            width = 32;
            height = 32;
        }

        int px = (int)object.getX() - radius;
        int py = (int)object.getY() - radius;
        int sx = (int)object.getX() + width + radius;
        int sy = (int)object.getY() + height + radius;

        for (IObject allObject : getObjects()) {
            if (main.distance(px, allObject.getX(), py, allObject.getY()) > maxDistance + main.distance(px, sx, py, sy))
                continue;
            for (int x = px; x <= sx; x++) {
                for (int y = py; y <= sy; y++) {
                    for (int[] hitbox : allObject.getBounds().getPixels()) {
                        if (allObject.getX() + hitbox[0] == x && allObject.getY() + hitbox[1] == y) {
                            objects.add(allObject);
                        }
                    }
                }
            }
        }


        return objects;
    }

	public Kid getKid() {
		if (kid == null) {
			System.out.println("No kid found, trying to find one.");
			for (IObject object : getObjects()) {
				if (object instanceof Kid) {
					kid = (Kid) object;
					break;
				}
			}
		}
		return kid;
	}

	public void setKid(Kid kid) {
		this.kid = kid;
	}

	public Main getMain() {
		return main;
	}
}