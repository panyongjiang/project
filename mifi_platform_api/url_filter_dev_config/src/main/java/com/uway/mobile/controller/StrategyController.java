package com.uway.mobile.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.uway.mobile.BaseApplication;
import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.domain.BlackWhiteList;
import com.uway.mobile.domain.Strategy;
import com.uway.mobile.service.StrategyService;
import com.uway.mobile.util.TraversalMapUtil;

@RestController
@RequestMapping("strategy")
public class StrategyController extends BaseApplication{
	
	@Autowired
	private StrategyService strategyService;
	@Autowired
	private TraversalMapUtil tm;
	
	/**
	 * 新增策略
	 * @param request
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="addStrategy", method = RequestMethod.POST)
	public Result addStrategy(HttpServletRequest request, @RequestBody Map<String, String> paraMap){
		Result result = new Result();
		Map<String, Object> paras=new HashMap<String, Object>();
		try{
			String userId = request.getAttribute("userId").toString();
			String seqNum = tm.getSeqNum(request);
			paras.put("seqNum", seqNum);
			String strategyName = paraMap.get("strategyName");
			Strategy strategy = new Strategy();
			strategy.setName(strategyName);
			strategy.setStatus("0");//未下发
			strategy.setCreateUser(Integer.parseInt(userId));
			strategy.setSecCheatWebsite("1");
			strategy.setSecHarmProgram("1");
			strategy.setSecIllegalGamble("1");
			strategy.setSecIllegalSite("1");
			strategy.setSecPhishingSite("1");
			strategy.setSecSexyInfo("1");
			strategy.setSecSexySite("1");
			strategy.setSecShamAdv("1");
			strategy.setSecTortContent("1");
			strategyService.insertStrategy(strategy);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setData(paras);
			result.setMsg("新增成功");
		}catch(Exception e){
			log.error("addStrategy", e);
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("新增失败");
		}
		return result;
	}
	
	/**
	 * 跳转到策略编辑页
	 * @param request
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="toEditStrategy", method = RequestMethod.POST)
	public Result toEditStrategy(HttpServletRequest request, @RequestBody Map<String, String> paraMap){
		Result result = new Result();
		Map<String, Object> paras=new HashMap<String, Object>();
		try{
			String seqNum = tm.getSeqNum(request);
			String strategyId = paraMap.get("strategyId");
			Strategy strategy = strategyService.getStrategyById(Integer.parseInt(strategyId));
			paras.put("strategy", strategy);
			paras.put("seqNum", seqNum);
			result.setData(paras);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("查询成功");
		}catch(Exception e){
			log.error("toEditStrategy", e);
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("查询失败");
		}
		return result;
	}
	
	/**
	 * 编辑策略
	 * @param request
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="editStrategy", method = RequestMethod.POST)
	public Result editStrategy(HttpServletRequest request, @RequestBody Strategy strategy){
		Result result = new Result();
		Map<String, Object> paras=new HashMap<String, Object>();
		try{
			String seqNum = tm.getSeqNum(request);
			paras.put("seqNum", seqNum);
			strategyService.updateStrategy(strategy);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setData(paras);
			result.setMsg("编辑成功");
		}catch(Exception e){
			Log.error("editStrategy", e);
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("编辑失败");
		}
		return result;
	}
	
	/**
	 * 删除策略
	 * @param request
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="deleteStrategy", method = RequestMethod.POST)
	public Result deleteStrategy(HttpServletRequest request, @RequestBody Map<String, String> paraMap){
		Result result = new Result();
		Map<String, Object> paras=new HashMap<String, Object>();
		try{
			String seqNum = tm.getSeqNum(request);
			paras.put("seqNum", seqNum);
			String strategyId = paraMap.get("strategyId");
			String msg = strategyService.deleteById(Integer.parseInt(strategyId));
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setData(paras);
			result.setMsg(msg);
		}catch(Exception e){
			log.error("deleteStrategy", e);
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("删除失败");
		}
		return result;
	}
	
	/**
	 *查询策略列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="findStrategys", method = RequestMethod.POST)
	public Result findStrategys(HttpServletRequest request){
		Result result = new Result();
		Map<String,Object> paras=new HashMap<String,Object>();
		try{
			String userId = request.getAttribute("userId").toString();
			String seqNum = tm.getSeqNum(request);
			paras.put("seqNum", seqNum);
			List<Strategy> strategyList = strategyService.getStrategysByUserId(Integer.parseInt(userId));
			paras.put("strategyList", strategyList);
			result.setData(paras);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("查询成功");
		}catch(Exception e){
			log.error("findStrategys", e);
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("查询失败");
		}
		return result;
	}
	
	/**
	 *  下发
	 * @param request
	 * @return
	 */
	@RequestMapping(value="order", method = RequestMethod.POST)
	public Result order(HttpServletRequest request, @RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		Map<String, Object> paras=new HashMap<String, Object>();
		try{
			String userId = request.getAttribute("userId").toString();
			String optSeq = tm.getSeqNum(request);
			paraMap.put("optSeq", optSeq);
			paraMap.put("userId", userId);
			paras.put("seqNum", optSeq);
			if(!paraMap.containsKey("userIds")&&!paraMap.containsKey("deviceIds")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("没有选择需要下发的设备");
				return result;
			}
			strategyService.order(paraMap);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setData(paras);
			result.setMsg("下发指令以下达，等待完成!");
		}catch(Exception e){
			log.error("order", e);
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("下发失败");
			result.setData(paras);
			
		}
		return result;
	}
	
	/**
	 * 添加黑白名单
	 * @param request
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="addBlackWhiteList", method = RequestMethod.POST)
	public Result addBlackWhiteList(HttpServletRequest request, @RequestBody BlackWhiteList blackWhiteList){
		Result result = new Result();
		Map<String, Object> paras=new HashMap<String, Object>();
		try{
			String seqNum = tm.getSeqNum(request);
			paras.put("seqNum", seqNum);
			strategyService.insertBlackWhiteList(blackWhiteList);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setData(paras);
			result.setMsg("操作成功");
		}catch(Exception e){
			log.error("addBlackWhiteList", e);
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("操作失败");
		}
		return result;
	}
	
	/**
	 * 删除黑白名单
	 * @param request
	 * @param paraMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="deleteBlackWhiteList", method = RequestMethod.POST)
	public Result deleteBlackWhiteList(HttpServletRequest request, @RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		Map<String, Object> paras=new HashMap<String, Object>();
		try{
			String seqNum = tm.getSeqNum(request);
			paras.put("seqNum", seqNum);
			List<Integer> listIds = (List<Integer>)paraMap.get("listIds");
			strategyService.deleteBlackWhiteList(listIds);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setData(paras);
			result.setMsg("删除成功");
		}catch(Exception e){
			log.error("deleteBlackWhiteList", e);
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("删除失败");
		}
		return result;
	}

}
