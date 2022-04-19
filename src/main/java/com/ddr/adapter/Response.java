package com.ddr.adapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

public class Response<T> {
	
	Map<String, Object> response;

	

	public Response(Page<T> pageTuts) {
		
		super();
		
		response = new LinkedHashMap<>();
		List<T> list = new ArrayList<T>();
		list = pageTuts.getContent();
		
		response.put("elements", list);
		response.put("hasPreviousPage", pageTuts.hasPrevious());
		response.put("currentPage", pageTuts.getNumber());
		response.put("hasNextPage", pageTuts.hasNext());
		response.put("totalItems", pageTuts.getTotalElements());
		response.put("totalPages", pageTuts.getTotalPages());
		response.put("sort", pageTuts.getSort());
		
		
		
	
	}
	
	public Map<String, Object> getResponse() {
		return response;
	}

	public void setResponse(Map<String, Object> response) {
		this.response = response;
	}

	@Override
	public String toString() {
		return "Response [response=" + response + "]";
	}
	
	

}
