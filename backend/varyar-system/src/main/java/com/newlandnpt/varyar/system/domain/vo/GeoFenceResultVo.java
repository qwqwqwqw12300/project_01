package com.newlandnpt.varyar.system.domain.vo;

/**
 * @author lisd
 * @date 2023/1/5
 **/
public class GeoFenceResultVo {

    /**
     * 围栏id
     */
    private Long gfid;

    /**
     * 围栏名称
     */
    private String gfname;

    /**
     * 坐标点是否在围栏区域内（0：不在 1：在）
     */
    private Integer in;

	public Long getGfid() {
		return gfid;
	}

	public void setGfid(Long gfid) {
		this.gfid = gfid;
	}

	public String getGfname() {
		return gfname;
	}

	public void setGfname(String gfname) {
		this.gfname = gfname;
	}

	public Integer getIn() {
		return in;
	}

	public void setIn(Integer in) {
		this.in = in;
	}

}
