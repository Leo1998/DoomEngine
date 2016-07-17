package com.doomengine.system;

import com.doomengine.app.Application;

public interface ContextAllocator {

	public DoomContext allocateContext(Application app);

}
