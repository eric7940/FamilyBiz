package com.fb.service.impl;

import java.util.List;

import com.fb.service.CommonService;
import com.fb.util.FamilyBizException;
import com.fb.vo.LabelValueBean;
import com.fb.vo.Lookup;

public class CommonServiceImpl extends ServiceImpl implements CommonService {

	@SuppressWarnings("unchecked")
	public List<LabelValueBean> getUnits() throws FamilyBizException {
		Lookup lookup = new Lookup();
		lookup.setLookupType("UNIT");
		lookup.setDisplayFlag(true);
		return this.getFbDao().queryForList("selectLookup", lookup);
	}

	public String getOfferDefaultMemo() throws FamilyBizException {
		Lookup lookup = new Lookup();
		lookup.setLookupType("OFFER_MEMO");
		lookup.setDisplayFlag(true);
		
		String memo = "";
		LabelValueBean result = (LabelValueBean) this.getFbDao().queryForObject("selectLookup", lookup);
		if (result != null) {
			memo = result.getLabel();
		}
		return memo;
	}
	
	public void modifyOfferDefaultMemo(String memo) throws FamilyBizException {
		Lookup lookup = new Lookup();
		lookup.setLookupType("OFFER_MEMO");
		this.getFbDao().delete("deleteLookup", lookup);

		lookup.setLookupCde("");
		lookup.setLookupNme(memo);
		lookup.setDisplayFlag(true);
		lookup.setDisplayOrder(0);
		lookup.setDscr("出貨單附註");
		this.getFbDao().insert("insertLookup", lookup);
	}
	
	public void modifyUnits(List<String> units) throws FamilyBizException {
		Lookup lookup = new Lookup();
		lookup.setLookupType("UNIT");
		this.getFbDao().delete("deleteLookup", lookup);

		for(int i = 0; i < units.size(); i++) {
			int idx = i + 1;
			lookup.setLookupCde("UT-" + idx);
			lookup.setLookupNme((String)units.get(i));
			lookup.setDisplayFlag(true);
			lookup.setDisplayOrder(idx);
			lookup.setDscr("單位-" + (String)units.get(i));
			this.getFbDao().insert("insertLookup", lookup);
		}
	}	
}
