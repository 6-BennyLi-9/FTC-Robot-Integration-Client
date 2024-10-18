package org.firstinspires.ftc.teamcode.ric.utils.PID;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.ric.utils.annotations.UserRequirementFunctions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PidContentPackage {
	public Map<String,PidContent> coreContents;
	public Set<String> angleBasicContentTag;

	public PidContentPackage(){
		coreContents =new HashMap<>();
		angleBasicContentTag=new HashSet<>();
	}

	@UserRequirementFunctions
	public void register(@NonNull PidContent content) throws IllegalArgumentException {
		if(coreContents.containsKey(content.tag)) {
			throw new IllegalArgumentException("Already Registered content,tag:"+content.tag);
		}
		coreContents.put(content.tag,content);
	}
	@UserRequirementFunctions
	public void register(@NonNull PidContent content,boolean isAngleBased){
		if(isAngleBased) angleBasicContentTag.add(content.tag);
		register(content);
	}
	@UserRequirementFunctions
	public PidContent getTag(String tag) throws ClassNotFoundException {
		if(coreContents.containsKey(tag)){
			return coreContents.get(tag);
		}
		throw new ClassNotFoundException("Unknown tag:"+tag);
	}
	@UserRequirementFunctions
	public boolean TagIsAngleBasedContent(String tag){
		return angleBasicContentTag.contains(tag);
	}

	@NonNull
	@Override
	public String toString() {
		StringBuilder res= new StringBuilder();
		for (Map.Entry<String, PidContent> entry : coreContents.entrySet()) {
			res.append("[").append(entry.getValue()).append("]\n");
		}
		return res.toString();
	}
}
