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
		this.coreContents =new HashMap<>();
		this.angleBasicContentTag =new HashSet<>();
	}

	@UserRequirementFunctions
	public void register(@NonNull final PidContent content) throws IllegalArgumentException {
		if(this.coreContents.containsKey(content.tag)) {
			throw new IllegalArgumentException("Already Registered content,tag:"+content.tag);
		}
		this.coreContents.put(content.tag,content);
	}
	@UserRequirementFunctions
	public void register(@NonNull final PidContent content, final boolean isAngleBased){
		if(isAngleBased) this.angleBasicContentTag.add(content.tag);
		this.register(content);
	}
	@UserRequirementFunctions
	public PidContent getTag(final String tag) throws ClassNotFoundException {
		if(this.coreContents.containsKey(tag)){
			return this.coreContents.get(tag);
		}
		throw new ClassNotFoundException("Unknown tag:"+tag);
	}
	@UserRequirementFunctions
	public boolean TagIsAngleBasedContent(final String tag){
		return this.angleBasicContentTag.contains(tag);
	}

	@NonNull
	@Override
	public String toString() {
		final StringBuilder res = new StringBuilder();
		for (final Map.Entry<String, PidContent> entry : this.coreContents.entrySet()) {
			res.append("[").append(entry.getValue()).append("]\n");
		}
		return res.toString();
	}
}
