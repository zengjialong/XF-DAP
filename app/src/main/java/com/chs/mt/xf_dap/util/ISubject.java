package com.chs.mt.xf_dap.util;

import java.util.*;

public interface ISubject {
	public void attach(IObserver observer, String event);
	public void detach(IObserver observer, String event);
	public void notify(HashMap<String ,Object> notification);
}
