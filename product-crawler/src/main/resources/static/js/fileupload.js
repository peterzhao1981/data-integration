/**
 * $(function(){ }是$(document).ready(function(){})的简写,相当于window.onload = function(){ }
 * 前者页面框架加载完成就执行，后者页面中所有内容加载完成才执行
 */
$(function() {
	var $list = $("#thelist");
	var fileSize = 0; //总文件大小
	var fileName = []; //文件名列表
	var fileSizeOneByOne = []; //每个文件大小
	var uploader; // 实例化   
	var currentFile;
	var index = 0; //当前上传文件序号
	var fileNum = 0; //待上传文件的数
	var existFlg='true';//是否首次上传
	uploader = WebUploader.create({
		auto: false,
		//是否自动上传
		pick: {
			id: '#multiUpload',
			label: '点击选择文件',
			name: "multiFile"
		},
		swf: '/resources/static/webuploader/Uploader.swf',
		fileVal: 'multiFile',
		//和name属性配合使用
		server: "/checkProduct/excelupload",
		duplicate: true,
		//同一文件是否可重复选择
		resize: false,
		formData: {
			"status": "multi",
			"contentsDto.contentsId": "0000004730",
			"uploadNum": "0000004730",
		},
		//指定类型
		accept:{title: 'Excel文件',
		    	extensions: 'xls,xlsx',
		    	mimeTypes: 'application/*'},
		compress: null,
		//图片不压缩
		chunked: true,
		//分片
		chunkSize: 20 * 1024 * 1024,
		//每片20M
		chunkRetry: false,
		//如果失败，则不重试
		threads: 1,
		//上传并发数。允许同时最大上传进程数。
		//fileNumLimit:50,//验证文件总数量, 超出则不允许加入队列
		// runtimeOrder: 'flash',  
		// 禁掉全局的拖拽功能。这样不会出现图片拖进页面的时候，把图片打开。  
		disableGlobalDnd: true
	});

	// 当有文件添加进来的时候
	uploader.on("fileQueued", function(file) {
		console.log("fileQueued:");
		fileNum = fileNum + 1;
		index=index+1;
		$list.append('<div id="' + file.id + '" class="item">' + '<h2 class="info">' + fileNum + ":" + file.name + ':' + '<p class="state">等待上传...</p>' + '</h2>' + '</div>');
	});
	// 当开始上传流程时触发
	uploader.on("startUpload", function() {	
		console.log("startUpload");
		//添加额外的表单参数
		$.extend(true, uploader.options.formData, {
			"fileSize": fileSize,
			"multiFileName": fileName,
			"fileSizeOneByOne": fileSizeOneByOne,
			"currentFile": currentFile,
			"fileNum": fileNum,
			"index": index,
			"existFlg": existFlg//是否首次上传
		});
			
	});
	
	function change(){
		existFlg="false";	
	}
	
	//当某个文件上传到服务端响应后，会派送此事件来询问服务端响应是否有效。
	uploader.on("uploadAccept", function(object, ret) {
		//服务器响应了
		//ret._raw  类似于 data
		console.log("uploadAccept");
		var data = JSON.parse(ret);
		console.log(ret);
		if (data != "1" && data != "3") {
			if (data == "9") {
				alert("error");
				uploader.reset();
				return;
			}
		} else {
			uploader.reset();
			alert("error");
		}
		index=0;//首次上传之后，将index置为0
	})

	uploader.on("uploadSuccess", function(file) {
		$("#" + file.id).find("p.state").text("已上传");
	});

	//出错之后要把文件从队列中remove调，否则，文件还在队里中，还是会上传到后台去
	uploader.on("uploadError", function(file, reason) {
		$("#" + file.id).find("p.state").text("上传出错");
		console.log("uploadError");
		console.log(file);
		console.log(reason);
		//多个文件
		var fileArray = uploader.getFiles();
		for (var i = 0; i < fileArray.length; i++) {
			//取消文件上传
			uploader.cancelFile(fileArray[i]);
			//从队列中移除掉
			uploader.removeFile(fileArray[i], true);
		}
		//发生错误重置webupload,初始化变量
		uploader.reset();
		fileSize = 0;
		fileName = [];
		fileSizeOneByOne = [];
	});

	//当validate不通过时，会以派送错误事件的形式通知调用者
	uploader.on("error", function() {
		console.log("error");
		uploader.reset();
		fileSize = 0;
		fileName = [];
		fileSizeOneByOne = [];
		alert("error 文件格式错误");
	});
	/**
	 * 多文件上传
	 */
	$("#startUpload").on("click", function() {
		if (existFlg=='true') {
			$.get("/checkProduct/clearFiles",function(){
				uploader.upload();
				change()
			});
		}else{
			uploader.upload();
			change()
		}
	});
	
	$("#clearFile").on("click", function() {
		$.messager.confirm('Confirm','将清空已导入的Excel文件，继续吗？',function(r){
			if (r) {
				$.messager.progress({title:'处理提示',
		    　　　　　　　　msg:'进度',
		    　　　　　　　　text:'努力清空文件中...',
		    　　　　　　　　interval:'600'});			
				    $.get("/checkProduct/clearFiles",function(data){
						$("#thelist").empty();	
						$.messager.progress('close');
						showMessage('清空文件完毕！',"info");
				});				
			}
		});
	});
	
	//任务完成之后的提示框
	function showMessage(info,type){
		$.messager.alert('提示',info,type);
	}
	//导入数据库之前将清空数据库。当Excel文件夹为空时，无需导入
	$("#ExcelToDB").on("click", function() {
		$.messager.confirm('Confirm','导入数据库将清空之前从EXCEL导入数据库的数据，继续吗？',function(r){
			if (r) {
				$.messager.progress({title:'处理提示',
		    　　　　　　　　msg:'进度',
		    　　　　　　　　text:'努力导入数据库中...',
		    　　　　　　　　interval:'600'});
				
				$.get("/checkProduct/checkExcels",function(info){
					var data=eval(info);
					if (data=="null"||data==""||data==undefined) {
						$.get("/checkProduct/deleteUserRecord",function(info){				
							if (info=="success") {
								$.post("/checkProduct/excelProcess",function(info){
									data=eval(info);
									if (data==1) {
										showMessage("导入数据库成功","info");
									}else{
										showMessage("导入失败","error");
									}
									$.messager.progress('close');
								});
							}
						});
					}else if (data[0]=="0"){//没有文件，无需导入
						showMessage("没有上传的文件","info");
						$.messager.progress('close');
					}else{
						showMessage("上传文件已清空，请重新上传所有文件。错误如下："+data,"error");
						$.messager.progress('close');
					}
					$("#datagrid").attr("url", "/checkProduct/getSearchResult");
				});
	
			}
		});
	});
	
	function exportToDB(){
		$.get("/checkProduct/deleteUserRecord",function(info){				
			if (info=="success") {
				$.post("/checkProduct/excelProcess",function(info){
					data=eval(info);
					if (data==1) {
						showMessage("导入数据库成功","info");
					}else{
						showMessage("导入失败","error");
					}
				});
			}
		});	
	}

	// 文件上传过程中创建进度条实时显示。
	uploader.on('uploadProgress', function(file, percentage) {
		var $li = $('#' + file.id),
			$percent = $li.find('.progress .progress-bar');

		// 避免重复创建
		if (!$percent.length) {
			$percent = $('<div class="progress progress-striped active">' + '<div class="progress-bar" role="progressbar" style="width: 0%">' + '</div>' + '</div>').appendTo($li).find('.progress-bar');
		}

		$li.find('p.state').text('上传中');

		$percent.css('width', percentage * 100 + '%');
	});
	uploader.on('uploadSuccess', function(file) {
		$('#' + file.id).find('p.state').text('已上传');
	});

	uploader.on('uploadError', function(file) {
		$('#' + file.id).find('p.state').text('上传出错');
	});

	uploader.on('uploadComplete', function(file) {
		$('#' + file.id).find('.progress').fadeOut();
	});


	/**
	 *取得每个文件的文件名和文件大小
	 */
	//选择文件之后执行上传  ,点击上传之后开始上传
	$(document).on("change", "input[id='startUpload']", function() {
		var fileArray1 = uploader.getFiles();
		var fileNames = [];
		for (var i = 0; i < fileArray1.length; i++) {
			fileNames.push(fileArray1[i].name); //input 框用
			//后台用
			currentFile = fileArray1[i];
			fileSize += fileArray1[i].size;
			fileSizeOneByOne.push(fileArray1[i].size);
			fileName.push(fileArray1[i].name);
		}
		console.log(fileSize);
		console.log(fileSizeOneByOne);
		console.log(fileName);
	})
});