/**
 * Dashboard JavaScript to show all recordings and replay records
 */

var host = 'localhost';
var port = '3000';
var app = angular.module('myApp', ['ngGrid'], function($locationProvider) {
    $locationProvider.html5Mode(true);
});
app.controller('RecordingCtrl', function($scope, $http) {
	 $scope.filterOptions = {
		        filterText: "",
		        useExternalFilter: true
		    };
		    $scope.totalServerItems = 0;
		    $scope.pagingOptions = {
		        pageSizes: [20],
		        pageSize: 20,
		        currentPage: 1
		    };  
		    $scope.setPagingData = function(data, page, pageSize){	
		        var pagedData = data.slice((page - 1) * pageSize, page * pageSize);
		        $scope.myData = pagedData;
		        $scope.totalServerItems = data.length;
		        if (!$scope.$$phase) {
		            $scope.$apply();
		        }
		    };
		    $scope.getPagedDataAsync = function (pageSize, page, searchText) {
		    	console.log("angular js");
		    	var getAllRecordingsURL = "http://" + host + ":" + port + "/getAllPosts";
		    	
		        setTimeout(function () {
		            $http.get(getAllRecordingsURL).success(function(data){
		            	$scope.setPagingData(data,page,pageSize);
		            });
		        }, 100);
		    };
			
		    $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage);
			
		    $scope.$watch('pagingOptions', function (newVal, oldVal) {
		        if (newVal !== oldVal && newVal.currentPage !== oldVal.currentPage) {
		          $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
		        }
		    }, true);
		    $scope.$watch('filterOptions', function (newVal, oldVal) {
		        if (newVal !== oldVal) {
		          $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
		        }
		    }, true);
			
		    $scope.gridOptions_recording = {
		        data: 'myData',
		        enablePaging: true,
		        showFooter: true,
		        enableColumnResize: true,
		        enableHighlighting: true,
		        totalServerItems:'totalServerItems',
		        pagingOptions: $scope.pagingOptions,
		        filterOptions: $scope.filterOptions,
		        selectedItems : []
		    };
});
