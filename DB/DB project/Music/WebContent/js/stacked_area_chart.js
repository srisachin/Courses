var labelType, useGradients, nativeTextSupport, animate;

(function() {
	var ua = navigator.userAgent, iStuff = ua.match(/iPhone/i)
			|| ua.match(/iPad/i), typeOfCanvas = typeof HTMLCanvasElement, nativeCanvasSupport = (typeOfCanvas == 'object' || typeOfCanvas == 'function'), textSupport = nativeCanvasSupport
			&& (typeof document.createElement('canvas').getContext('2d').fillText == 'function');
	// I'm setting this based on the fact that ExCanvas provides text support
	// for IE
	// and that as of today iPhone/iPad current text support is lame
	labelType = (!nativeCanvasSupport || (textSupport && !iStuff)) ? 'Native'
			: 'HTML';
	nativeTextSupport = labelType == 'Native';
	useGradients = nativeCanvasSupport;
	animate = !(iStuff || !nativeCanvasSupport);
})();

var Log = {
	elem : false,
	write : function(text) {
		if (!this.elem)
			this.elem = document.getElementById('log');
		this.elem.innerHTML = text;
		this.elem.style.left = (500 - this.elem.offsetWidth / 2) + 'px';
	}
};

function initChart(json) {
	$('#trend_graph-canvaswidget').remove();
	$('#trend_graph-label').remove();
	// init data
	// var json = {
	// 'label' : [ 'label A', 'label B', 'label C', 'label D' ],
	// 'values' : [ {
	// 'label' : 'date A',
	// 'values' : [ 20, 40, 15, 5 ]
	// }, {
	// 'label' : 'date B',
	// 'values' : [ 30, 10, 45, 10 ]
	// }, {
	// 'label' : 'date E',
	// 'values' : [ 38, 20, 35, 17 ]
	// }, {
	// 'label' : 'date F',
	// 'values' : [ 58, 10, 35, 32 ]
	// }, {
	// 'label' : 'date D',
	// 'values' : [ 55, 60, 34, 38 ]
	// }, {
	// 'label' : 'date C',
	// 'values' : [ 26, 40, 25, 40 ]
	// } ]
	//
	// };

	// end
	var infovis = document.getElementById('trend_graph');
	// init AreaChart
	var areaChart = new $jit.AreaChart({
		// id of the visualization container
		injectInto : 'trend_graph',
		// add animations
		animate : true,
		// separation offsets
		Margin : {
			top : 5,
			left : 5,
			right : 5,
			bottom : 5
		},
		labelOffset : 10,
		// whether to display sums
		showAggregates : true,
		// whether to display labels at all
		showLabels : true,
		// could also be 'stacked'
		type : useGradients ? 'stacked:gradient' : 'stacked',
		// label styling
		Label : {
			type : labelType, // can be 'Native' or 'HTML'
			size : 12,
			family : 'Arial',
			color : 'black'
		},
		// enable tips
		Tips : {
			enable : true,
			onShow : function(tip, elem) {
				tip.innerHTML = "<b>" + elem.name + "</b>: " + elem.value;
			}
		},
		// add left and right click handlers
		filterOnClick : true,
		restoreOnRightClick : true
	});
	// load JSON data.
	areaChart.loadJSON(json);
	// end
//	var list1 = $jit.id('id-list1'), list2 = $jit.id('id-list2'), list3 = $jit
//			.id('id-list3'), button = $jit.id('update'), restoreButton = $jit
//			.id('restore');
	var list1 = $jit.id('id-list1'), button = $jit.id('update'), restoreButton = $jit
	.id('restore');
	// restore graph on click
	$jit.util.addEvent(restoreButton, 'click', function() {
		areaChart.restore();
	});
	// dynamically add legend to list
//	var legend = areaChart.getLegend(), listItems1 = [], listItems2 = [], listItems3 = [];
	var legend = areaChart.getLegend(), listItems1 = [], finalList = [];
	var countOfName = 0;
	for ( var name in legend) {
//		listItems1
//		.push('<div class=\'query-color\' style=\'background-color:'
//				+ legend[name] + '\'>&nbsp;</div>&nbsp;&nbsp;' + name);
		
		listItems1
		.push( name);
		
//		if (countOfName % 3 == 0) {
//			listItems1
//					.push('<div class=\'query-color\' style=\'background-color:'
//							+ legend[name] + '\'>&nbsp;</div>' + name);
//		} else if (countOfName % 3 == 1) {
//			listItems2
//					.push('<div class=\'query-color\' style=\'background-color:'
//							+ legend[name] + '\'>&nbsp;</div>' + name);
//		} else {
//			listItems3
//					.push('<div class=\'query-color\' style=\'background-color:'
//							+ legend[name] + '\'>&nbsp;</div>' + name);
//		}
//		countOfName = countOfName + 1;
	}
//	for ( var name in legend) {
//		listItems1
//		.push('<div class=\'query-color\' style=\'background-color:'
//				+ legend[name] + '\'>&nbsp;</div>&nbsp;&nbsp;' + name);
////		if (countOfName % 3 == 0) {
////			listItems1
////					.push('<div class=\'query-color\' style=\'background-color:'
////							+ legend[name] + '\'>&nbsp;</div>' + name);
////		} else if (countOfName % 3 == 1) {
////			listItems2
////					.push('<div class=\'query-color\' style=\'background-color:'
////							+ legend[name] + '\'>&nbsp;</div>' + name);
////		} else {
////			listItems3
////					.push('<div class=\'query-color\' style=\'background-color:'
////							+ legend[name] + '\'>&nbsp;</div>' + name);
////		}
////		countOfName = countOfName + 1;
//	}
	var names = listItems1.slice(0, 10);
	var links = listItems1.slice(20, 30);
	
	for (i = 0; i < 10; i++) {
		finalList
		.push('<div class=\'query-color\' style=\'background-color:'
				+ legend[names[i]] + '\'>&nbsp;</div>&nbsp;&nbsp; <a href="'+links[i]+'" target="_blank">'+ names[i]+'</a> ');
	}
	
	list1.innerHTML = '<li style="margin-bottom: 10px;">' + finalList.join('</li><li style="margin-bottom: 10px;">') + '</li>';
	//list2.innerHTML = '<li>' + listItems2.join('</li><li>') + '</li>';
	//list3.innerHTML = '<li>' + listItems3.join('</li><li>') + '</li>';
}
