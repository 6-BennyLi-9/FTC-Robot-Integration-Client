package org.firstinspires.ftc.teamcode.utils.PID;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;

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
		if(coreContents.containsKey(content.Tag)) {
			throw new IllegalArgumentException("Already Registered content,Tag:"+content.Tag);
		}
		coreContents.put(content.Tag,content);
	}
	@UserRequirementFunctions
	public void register(@NonNull PidContent content,boolean isAngleBased){
		if(isAngleBased) angleBasicContentTag.add(content.Tag);
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
}
