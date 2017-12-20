package com.uway.mobile.util;

import java.util.List;

public class Pagination {

	private int pageNo;
	private int pageSize;
	private long total_num;
	private int totalPages;
	private List<?> details;

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotal_num() {
		return total_num;
	}

	public void setTotal_num(long total_num) {
		this.total_num = total_num;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public List<?> getDetails() {
		return details;
	}

	public void setDetails(List<?> details) {
		this.details = details;
	}

}
