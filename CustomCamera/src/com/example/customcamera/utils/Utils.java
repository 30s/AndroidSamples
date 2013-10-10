package com.example.customcamera.utils;

import java.util.ArrayList;

import android.media.CamcorderProfile;

public class Utils {
	public static ArrayList<Integer> getVideoFrameSize() {
		CamcorderProfile cp = CamcorderProfile
				.get(CamcorderProfile.QUALITY_LOW);
		try {
			CamcorderProfile cp480 = CamcorderProfile
					.get(CamcorderProfile.QUALITY_480P);
			if (cp480 != null) {
				cp = cp480;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<Integer> ret = new ArrayList<Integer>();
		ret.add(cp.videoFrameWidth);
		ret.add(cp.videoFrameHeight);
		return ret;
	}
}
