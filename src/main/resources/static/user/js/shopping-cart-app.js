
const app = angular.module("shopping-cart-app", []);

app.controller("shopping-cart-ctrl", function($scope, $http) {

	/*
	* Quản lý giỏ hàng
	*/

	$scope.cart = {
		items: [],

		myFunction(name){
				var item = this.items.find(item => item.name == name);
				const x = document.getElementsByName("qty");
	            for (let i = 0; i < x.length; i++) {
	                if (x[i].value > 20) {
	                    //alert("Số lượng mua giới hạn là 20.Để mua nhiều hơn vui lòng liên hệ đến số điện thoại 0901301277 hoặc đến trực tiếp của hàng để trao đổi.Xin cảm ơn!");
	                    document.getElementById('quantityValid').innerHTML="Số lượng mua giới hạn là 20.Để mua nhiều hơn vui lòng liên hệ đến số điện thoại 0901301277 hoặc đến trực tiếp của hàng để trao đổi.Xin cảm ơn!";
	                    //x[i].value = 1;
	                    item.qty = 1;
	                }else if(x[i].value>item.quantity){
						//alert("Hết hàng! Số lượng "+ item.name+ " chỉ còn lại "+ item.quantity 
						//+". Bạn vui lòng quay lại sau.Thông cảm cho sự bất tiện này");
						document.getElementById('quantityValid').innerHTML="Hết hàng! Số lượng "+ item.name+ " chỉ còn lại "+ item.quantity 
						+". Bạn vui lòng quay lại sau.Thông cảm cho sự bất tiện này";
						//x[i].value = 1;
						item.qty = 1;
					}else{
						document.getElementById('quantityValid').innerHTML="";
						//$scope.cart.loadFromLocalStorage();
						this.saveToLocalStorage();
					}
	            }
		},
		
		// thêm sp vào giỏ hàng
		add(product_id) {

			//Kiểm tra mặt hàng đó đã có trong giỏ hàng hay chưa?
			var item = this.items.find(item => item.product_id == product_id);
			//Nếu sản phẩm đã có trong giỏ  
			if (item) {
				//Lưu vào local//tăng số lượng
				if ($scope.cart.count > 50) {
					alert("Số lượng quá nhiều");
				} else if (item.qty > 19) {
					alert("Số lượng mua giới hạn là 20.Để mua nhiều hơn vui lòng liên hệ đến số điện thoại 0901301277 hoặc đến trực tiếp của hàng để trao đổi.Xin cảm ơn!");
				}
				else if (item.qty > item.quantity - 1) {
					alert("Hết hàng! Bạn vui lòng quay lại sau");
				}
				else {
					item.qty++;
					alert("bạn vừa thêm sản phẩm " + item.name + " vào giỏ hàng");
					this.saveToLocalStorage();
				}
			} else {

				//Ngược lại --> Tải sản phẩm trên server thông qua API
				$http.get(`/rest/products/${product_id}`).then(resp => {
					if ($scope.cart.count > 50) {
						alert("Số lượng quá nhiều");
					} else if (resp.data.qty > 19) {
						alert("Số lượng mua giới hạn là 20.Để mua nhiều hơn vui lòng liên hệ đến số điện thoại 0901301277 hoặc đến trực tiếp của hàng để trao đổi.Xin cảm ơn!");
					}
					else if (resp.data.qty > resp.data.quantity - 1) {
						alert("Hết hàng! Bạn Vui lòng quay lại sau");
					}
					else {
						//Đặt số lượng bằng 1
						resp.data.qty = 1;
						//Thêm sản phẩm vào giỏ
						this.items.push(resp.data);
						alert("bạn vừa thêm sản phẩm " + resp.data.name + " vào giỏ hàng");
						//Lưu vào local
						this.saveToLocalStorage();
					}


				})

			}

		},
		// xóa sản phẩm khỏi giỏ hàng
		remove(product_id) {
			// tìm vị trí index trong cart thông qua id
			// dùng splice để xóa 
			// gọi saveToLocalStorage() để lưu lại
			var index = this.items.findIndex(item => item.product_id == product_id);
			this.items.splice(index, 1);
			this.saveToLocalStorage();
		},
		// xóa sạch các mặt hàng trong giỏ
		clear() { // Xóa sạch các mặt hàng trong giỏ
			this.items = []
			this.saveToLocalStorage();
		},
		//tổng số lượng các mặt hàng trong giỏ
		get count() {
			return this.items
				.map(item => item.qty)
				.reduce((total, qty) => total += qty, 0);
		},
		//tổng thành tiền các mặt hàng trong giỏ
		get amount() {
			return this.items
				.map(item => item.qty * (item.price - (item.discount.discount * item.price) / 100))
				.reduce((total, qty) => total += qty, 0);
		},
		// lưu cart vào local storage
		saveToLocalStorage() {
			//đổi các mặt hàng sang json
			//dùng angular để copy xong 
			//dùng json để để lưu vào json có tên là cart
			var json = JSON.stringify(angular.copy(this.items));
			localStorage.setItem("cart", json);
			
		},
		// đọc giỏ hàng từ local storage
		loadFromLocalStorage() {
			var json = localStorage.getItem("cart");
			this.items = json ? JSON.parse(json) : [];
		},
	}
	//tải lại toàn bỏ mặt hàng vào trong giỏ hàng để hiển thị
	$scope.cart.loadFromLocalStorage();
	//đặt hàng		
	$scope.order = {
		account: { account_id: $("#account_id").val() },
		//payment_method: { payment_method_id:$("#payment_method_id").val()},
		payment_method: { payment_method_id: "" },
		orderdate: "",
		deliveryDate: "",
		orderStatus: "Chưa thanh toán",
		notes: "",
		phone: "",
		address: "",
		//shippingFee: $scope.cart.amount > 500000 ? shippingFee = 0 : shippingFee = 35000,
		totalPrice: $scope.cart.amount,
		get orderDetails() {
			return $scope.cart.items.map(item => {
				return {
					product: { product_id: item.product_id },
					totalPrice: (item.price - (item.discount.discount * item.price) / 100),
					totalQuantity: item.qty,
				}
			})
		},
		//gửi thông tin order lên server
		purchase() {
			if (document.getElementById("phone").value == "") {
				alert("Chưa nhập số điện thoại")
				return;
			}if(!document.getElementById("phone").value.match(/^\d{10}$/)){
				alert("Số điện thoại không hợp lệ");
				return;
			} if (document.getElementById("address").value == "") {
				alert("Chưa nhập địa chỉ")
				return;
			}
			
			var v = grecaptcha.getResponse();
		    if(v.length == 0)
		    {
		       // document.getElementById('captcha').innerHTML="You can't leave Captcha Code empty";
		       alert("Vui lòng xác nhận captcha");
		        return;
		    } 
			if (document.getElementById("acc-or").checked === false && document.getElementById("paypal").checked === false) {
				alert("Vui lòng chọn phương thức thanh toán")
				return;
			}
			var order = angular.copy(this);
			//Thực hiện đặt hàng
			$http.post("/rest/orders", order).then(resp => {
				alert("Đặt hàng thành công!");
				//$scope.cart.clear();
			//	if(document.getElementById("acc-or").checked === true){
					location.href = "/order/detail/" + resp.data.order_id;
			//}else if(document.getElementById("paypal").checked === true){
				//	location.href = "/thanh-toan";
				//}
				
			}).catch(error => {
				alert("Đặt hàng lỗi!")
				console.log(error)
			})
			$scope.cart.clear();
		}
	}



});
