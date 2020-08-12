import ApiService from "../ApiService";
import ValidationException from "../exception/ValidationException";

class EntryService extends ApiService {
	constructor() {
		super("/api/entries");
	}

	filter(entry) {
		let filters = `?user=${entry.user}&year=${entry.year}`;

		if (entry.month) {
			filters = `${filters}&month=${entry.month}`;
		}

		if (entry.type) {
			filters = `${filters}&type=${entry.type}`;
		}

		if (entry.status) {
			filters = `${filters}&status=${entry.status}`;
		}

		if (entry.description) {
			filters = `${filters}&description=${entry.description}`;
		}

		return this.get(filters);
	}

	find(id) {
		return this.get(`/${id}`);
	}

	save(entry) {
		return this.post("/", entry);
	}

	update(entry) {
		return this.put(`/${entry.id}`, entry);
	}

	deleteEntry(id) {
		return this.delete(`/${id}`);
	}

	updateStatus(id, status) {
		return this.put(`/${id}/update-status`, { status });
	}

	validate(entry) {
		const erros = [];

		if (!entry.description) {
			erros.push("Description is required");
		}

		if (!entry.year) {
			erros.push("Year is required");
		}

		if (!entry.month) {
			erros.push("Month is required");
		}

		if (!entry.amount) {
			erros.push("Amount is required");
		}

		if (!entry.type) {
			erros.push("Type is required");
		}

		if (erros && erros.length > 0) {
			throw new ValidationException(erros);
		}
	}

	getMonthList() {
		return [
			{ label: "Choose...", value: "" },
			{ label: "January", value: 1 },
			{ label: "February", value: 2 },
			{ label: "March", value: 3 },
			{ label: "April", value: 4 },
			{ label: "May", value: 5 },
			{ label: "June", value: 6 },
			{ label: "July", value: 7 },
			{ label: "August", value: 8 },
			{ label: "September", value: 9 },
			{ label: "October", value: 10 },
			{ label: "November", value: 11 },
			{ label: "December", value: 12 },
		];
	}

	getTypeList() {
		return [
			{ label: "Choose...", value: "" },
			{ label: "Expense", value: "EXPENSE" },
			{ label: "Revenue", value: "REVENUE" },
		];
	}

	getStatusList() {
		return [
			{ label: "Choose...", value: "" },
			{ label: "Pending", value: "PENDING" },
			{ label: "Canceled", value: "CANCELED" },
			{ label: "Made", value: "MADE" },
		];
	}
}
export default EntryService;
