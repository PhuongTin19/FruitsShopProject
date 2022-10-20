
const app = angular.module("shopping-cart-app", []);

app.controller("shopping-cart-ctrl", function($scope, $http) {
	/*
	* Quản lý giỏ hàng
	*/
	$scope.cart = {
		items: [],

		// thêm sp vào giỏ hàng
		add(product_id) {
			alert("bạn vừa thêm sản phẩm " + product_id + " vào giỏ hàng");
			//Kiểm tra mặt hàng đó đã có trong giỏ hàng hay chưa?
			var item = this.items.find(item => item.product_id == product_id);

			//Nếu sản phẩm đã có trong giỏ  
			if (item) {
				//Lưu vào local//tăng số lượng
				item.qty++;
				this.saveToLocalStorage();
			} else {
				//Ngược lại --> Tải sản phẩm trên server thông qua API
				$http.get(`/rest/products/${product_id}`).then(resp => {
					//Đặt số lượng bằng 1
					resp.data.qty = 1;
					//Thêm sản phẩm vào giỏ
					this.items.push(resp.data);
					//Lưu vào local
					this.saveToLocalStorage();
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
			//dùng json để để lưu vào jason có tên là cart

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
		payment_method: { payment_method_id: $("#payment_method_id").val() },
		orderdate: "",
		deliveryDate: "",
		orderStatus: "Đang vận chuyển",
		notes: "",
		phone: "",
		address: "",
		shippingFee: $scope.cart.amount > 500000 ? shippingFee = 0 : shippingFee = 35000,
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
			var order = angular.copy(this);
			//Thực hiện đặt hàng
			$http.post("/rest/orders", order).then(resp => {
				alert("Đặt hàng thành công!");
				$scope.cart.clear();
				location.href = "/order/detail/" + resp.data.order_id;
			}).catch(error => {
				alert("Đặt hàng lỗi!")
				console.log(error)
			})
		}
	}
});
