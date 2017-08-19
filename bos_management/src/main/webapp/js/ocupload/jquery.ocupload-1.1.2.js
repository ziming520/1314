/*
 * One Click Upload - jQuery Plugin
 * Copyright (c) 2008 Michael Mitchell - http://www.michaelmitchell.co.nz
 * 附注释翻译
 */
(function($){
	$.fn.upload = function(options) {
		/** Merge the users options with our defaults */
		/**将用户选项与默认值合并,后者option值覆盖前者默认值*/
		options = $.extend({
			name: 'file',
			enctype: 'multipart/form-data',
			action: '',
			autoSubmit: true,
			onSubmit: function() {},
			onComplete: function() {},
			onSelect: function() {},
			params: {}
		}, options);

		return new $.ocupload(this, options);
	},
	
	$.ocupload = function(element, options) {
		/** Fix scope problems */
		/** 固定范围的问题*/
		var self = this;
	
		/** A unique id so we can find our elements later */
		/** 一个唯一的ID，所以之后我们才可以找到我们的元素*/
		var id = new Date().getTime().toString().substr(8);
		
		/** Upload Iframe */
		/** 上传页内框架*/
		var iframe = $(
			'<iframe '+
				'id="iframe'+id+'" '+
				'name="iframe'+id+'"'+
			'></iframe>'
		).css({
			display: 'none'
		});
		
		/** Form */
		var form = $(
			'<form '+
				'method="post" '+
				'enctype="'+options.enctype+'" '+
				'action="'+options.action+'" '+
				'target="iframe'+id+'"'+
			'></form>'
		).css({
			margin: 0,
			padding: 0
		});
		
		/** File Input */
		var input = $(
			'<input '+
				'name="'+options.name+'" '+
				'type="file" '+
			'/>'
		).css({
			position: 'relative',
			display: 'block',
			marginLeft: -175+'px',
			opacity: 0
		});
		
		/** Put everything together */
		/** 把所有东西都放在一起*/
		element.wrap('<div></div>'); //container
			form.append(input);
			element.after(form);
			element.after(iframe);
	
		/** Find the container and make it nice and snug */
		/** 找到容器，让它最合适*/
		var container = element.parent().css({
			position: 'relative',
			height: element.outerHeight()+'px',
			width: element.outerWidth()+'px',
			overflow: 'hidden',
			cursor: 'pointer',
			margin: 0,
			padding: 0
		});
			
		/** Put our file input in the right place */
		/** 把我们的文件输入在正确的地方*/
		input.css('marginTop', -container.height()-10+'px');

		/** Move the input with the mouse to make sure it get clicked! */
		/** 移动输入用鼠标去点击确定！*/
		container.mousemove(function(e){
			input.css({
				top: e.pageY-container.offset().top+'px',
				left: e.pageX-container.offset().left+'px'
			});
		});
		
		/** Watch for file selection */
		/** 注意文件选择*/
		input.change(function() {
			/** Do something when a file is selected. */
			/** 在选择文件时做一些事情。*/
			self.onSelect(); 
			
			/** Submit the form automaticly after selecting the file */
			/** 提交表单后自行选择文件*/
			if(self.autoSubmit) {
				self.submit();
			}
		});
		
		/** Methods */
		$.extend(this, {
			autoSubmit: true,
			onSubmit: options.onSubmit,
			onComplete: options.onComplete,
			onSelect: options.onSelect,
		
			/** get filename */		
			filename: function() {
				return input.attr('value');
			},
			
			/** get/set params */
			/** 参数数组*/
			params: function(params) {
				var params = params ? params : false;
				
				if(params) {
					options.params = $.extend(options.params, params);
				}
				else {
					return options.params;
				}
			},
			
			/** get/set name */
			name: function(name) {
				var name = name ? name : false;
				
				if(name) {
					input.attr('name', value);
				}
				else {
					return input.attr('name');
				}
			},
			
			/** get/set action */
			action: function(action) {
				var action = action ? action : false;
				
				if(action) {
					form.attr('action', action);
				}
				else {
					return form.attr('action');
				}
			},
			
			/** get/set enctype */
			/** 编码类型 */
			enctype: function(enctype) {
				var enctype = enctype ? enctype : false;
				
				if(enctype) {
					form.attr('enctype', enctype);
				}
				else {
					return form.attr('enctype');
				}
			},
			
			/** set options */
			set: function(obj, value) {
				var value =	value ? value : false;
								
				function option(action, value) {
					switch(action) {
						default:
							throw new Error('[jQuery.ocupload.set] \''+action+'\' is an invalid option.');
							break;
						case 'name':
							self.name(value);
							break;
						case 'action':
							self.action(value);
							break;
						case 'enctype':
							self.enctype(value);
							break;
						case 'params':
							self.params(value);
							break;
						case 'autoSubmit':
							self.autoSubmit = value;
							break;
						case 'onSubmit':
							self.onSubmit = value;
							break;
						case 'onComplete':
							self.onComplete = value;
							break;
						case 'onSelect':
							self.onSelect = value;
							break;
					}
				}				
				
				if(value) {
					option(obj, value);
				}
				else {				
					$.each(obj, function(key, value) {
						option(key, value);
					});
				}
			},
			
			/** Submit the form */
			submit: function() {
				/** Do something before we upload */
				/** 在上传之前做一些事情*/
				this.onSubmit();
				
				/** add additional paramters before sending */
				/** 在发送之前添加额外的参数*/
				$.each(options.params, function(key, value) {
					form.append($(
						'<input '+
							'type="hidden" '+
							'name="'+key+'" '+
							'value="'+value+'" '+
						'/>'
					));
				});
				
				/** Submit the actual form */
				/** 提交实际表格*/
				form.submit(); 
				
				/** Do something after we are finished uploading */
				/** 当我们完成上传做某事*/
				iframe.unbind().load(function() {
					/** Get a response from the server in plain text */
					/** 从纯文本服务器的响应*/
					var myFrame = document.getElementById(iframe.attr('name'));
					var response = $(myFrame.contentWindow.document.body).text();
					
					/** Do something on complete */
					/** 在完成做某事*/
					self.onComplete(response); //done :D
				});
			}
		});
	}
})(jQuery);