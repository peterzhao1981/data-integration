/**
 * 这是主页的js事件
 */
var mainPage = new function() {
		var excelUploadPage = "/checkProduct/excelUploadPage";

		return {
			loadExcel: function() {
				$.get("/checkProduct/checkThread", function(info) {
					if (false == info) {
						var pageIframe = $("#pageIframe");
						pageIframe.attr("id", "pageIframe");
						pageIframe.attr("frameborder", "no");
						pageIframe.attr("border", "0");
						pageIframe.attr("marginwidth", "0");
						pageIframe.attr("marginheight", "0");
						pageIframe.attr("style", "width:100%;height:100%");
						pageIframe.attr("scrolling", "yes");
						pageIframe.attr("src", excelUploadPage);

						$('#modal').dialog({
							title: 'Excel文件上传',
							width: 600,
							height: 400,
							closed: false,
							cache: false,
							maximizable: true,
							minimizable: false,
							resizable: false,
							modal: true
						});
					} else {
						$.messager.alert('提示', "爬虫正在运行中请稍后再试", "warning");
					}
				})
			},

			startRun: function() {
				$.get("/checkProduct/checkThread", function(info) {
					if (false == info) {
						$.messager.confirm('Confirm', '将开始爬取数据，确定吗？', function(r) {
							if (r) {
								$.messager.progress({
									title: '处理提示',
									　　　　　　　　msg: '进度',
									　　　　　　　　text: '疯狂爬取中...',
									　　　　　　　　interval: '600'
								});
								$.get("/checkProduct/checkProductStatus", function(info) {
									if (info == "success") {
										$.get("/checkProduct/get1688APIUseCurrency", function(info) {
											if (info == "error") {
												$.messager.alert('处理结果消息', '处理完成,但是1688API调用达到上限，请过24小时再继续处理', 'warning');
											} else {
												$.messager.alert('处理结果消息', '处理完成', 'info');
											}
										})
									} else {
										$.messager.alert('处理结果消息', '处理完成,但出现异常', 'warning');
									}
									$("#datagrid").attr("url", "/checkProduct/getSearchResult");
									$.messager.progress("close");
								});
							}
						});
					} else {
						$.messager.alert('提示', "爬虫正在运行中请稍后再试", "warning");
					}
				})
			},

			exportExcelLess: function() {
				$.get("/checkProduct/checkThread", function(info) {
					if (false == info) {
						$.messager.confirm('Confirm', '将导出商品url有效性列表信息，确定吗？', function(r) {
							if (r) {
								$("#moreForm").remove();
								var form = $("<form>");
								form.attr("style", "display:none");
								form.attr("id", "LessForm");
								form.attr("target", "");
								form.attr("method", "post");
								form.attr("action", "/checkProduct/exportDBtoExcel/1");
								var input1 = $("<input>");
								input1.attr("type", "hidden");
								$("body").append(form);
								form.append(input1);
								form.submit();
							}
						});
					} else {
						$.messager.alert('提示', "爬虫正在运行中请稍后再试", "warning");
					}
				})
			},

			exportExcelMore: function() {
				$.messager.confirm('Confirm', '将导出包含1688商品缺货信息的url有效性信息，确定吗？', function(r) {
					if (r) {
						$.get("/checkProduct/checkThread", function(info) {
							if (false == info) {
								$("LessForm").remove();
								var form = $("<form>");
								form.attr("style", "display:none");
								form.attr("id", "moreForm");
								form.attr("target", "");
								form.attr("method", "post");
								form.attr("action", "/checkProduct/exportDBtoExcel/2");
								var input1 = $("<input>");
								input1.attr("type", "hidden");
								$("body").append(form);
								form.append(input1);
								form.submit();
							} else {
								$.messager.alert('提示', "爬虫正在运行中请稍后再试", "warning");
							}
						});
					}
				});
			}
		}
	}();