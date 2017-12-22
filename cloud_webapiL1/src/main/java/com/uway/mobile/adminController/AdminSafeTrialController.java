package com.uway.mobile.adminController;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.service.SafeTrialService;
import com.uway.mobile.util.ObjectUtil;

@RestController
@RequestMapping("admin_safe_trial")
public class AdminSafeTrialController {
	@Autowired
	private SafeTrialService safeTrialService;

	/**
	 * 查询所有的试用申请
	 * @param request
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/list_safe_trial", method = RequestMethod.POST)
	public Result listNote(HttpServletRequest request, @RequestBody Map<String, Object> paraMap) throws Exception{
		Result result = new Result();
		try {
			if (paraMap.get("page_size") == null) {
				paraMap.put("page_size", Constance.PAGE_SIZE);
			} else {
				paraMap.put("page_size", Integer.parseInt(paraMap.get("page_size").toString()));
			}
			if (paraMap.get("page_num") == null) {
				paraMap.put("page_num", 0);
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("请传入页码！");
				return result;
			} else {
				paraMap.put("page_num",
						(Integer.parseInt(paraMap.get("page_num").toString()) - 1)
								* Integer.parseInt(paraMap.get("page_size").toString()));
			}
			
			if(Integer.parseInt(paraMap.get("page_num").toString()) < 0 || Integer.parseInt(paraMap.get("page_size").toString()) <= 0){
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				result.setMsg("参数格式不正确！");
				return result;
			}
			result.setData(safeTrialService.getAllSafeTrial(paraMap));
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("查询成功！");
		} catch (Exception e) {
			
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误！");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 申请详情
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/safe_trial_details", method=RequestMethod.POST)
	public Result safeTrialDetails(@RequestBody Map<String, Object> paraMap) throws Exception{
		Result result = new Result();
		try{
			result.setData(safeTrialService.getSafeTrialById(paraMap));
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("查询成功！");
		}catch(Exception e){
			
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误！");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 审核使试用申请
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/audit_safe_trial", method=RequestMethod.POST)
	public Result AuditSafeTrial(@RequestBody Map<String, Object> paraMap) throws Exception{
		Result result = new Result();
		try {
			if(!ObjectUtil.isEmpty(paraMap, "trial_status") ){
				if(!ObjectUtil.isEmpty(paraMap, "id")){
					int num = 0;
					num = safeTrialService.UpdateTrialStatusById(paraMap);
					if(num > 0){
						result.setCode(Constance.RESPONSE_SUCCESS);
						result.setMsg("审核通过！");
					}else{
						result.setCode(Constance.RESPONSE_INNER_ERROR);
						result.setMsg("内部错误！");
					}
				}else{
					result.setCode(Constance.RESPONSE_PARAM_EMPTY);
					result.setMsg("请传试用申请id!");
				}
			}else{
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("参数为空!");
			}
		} catch (Exception e) {
			
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误！");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 删除试用申请
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/del_safe_trial", method=RequestMethod.POST)
	public Result delSafeTrial(@RequestBody Map<String, Object> paraMap) throws Exception{
		Result result = new Result();
		try {
			if(!ObjectUtil.isEmpty(paraMap, "id")){
				int num = 0;
				num = safeTrialService.delById(paraMap);
				if(num > 0){
					result.setCode(Constance.RESPONSE_SUCCESS);
					result.setMsg("删除成功！");
				}else{
					result.setCode(Constance.RESPONSE_INNER_ERROR);
					result.setMsg("内部错误！");
				}
			}else{
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("参数为空！");
			}		
		} catch (Exception e) {			
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误！");
			e.printStackTrace();
		}
		return result;
	}
}
