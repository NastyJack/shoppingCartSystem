import json
from collections import Counter, defaultdict
from reportlab.lib.pagesizes import A4
from reportlab.pdfgen import canvas
from reportlab.lib.units import inch

LOG_FILE = "../nodeAPILayer/logs/api_requests.log"
REPORT_FILE = "shopping_report.pdf"

PRICES = {
    "Apple": 35,
    "Banana": 20,
    "Melon": 50,   # Buy one get one free
    "Lime": 15     # 3-for-2 offer
}

def calculate_discounted_total(cart):
    counter = Counter(cart)
    total = 0
    for item, qty in counter.items():
        if item == "Melon":
            total += ((qty // 2) + (qty % 2)) * PRICES[item]
        elif item == "Lime":
            total += ((qty // 3) * 2 + qty % 3) * PRICES[item]
        else:
            total += qty * PRICES[item]
    return total

def analyze_log(filepath):
    cart = []
    cart_snapshots = []
    item_add_freq = Counter()
    item_sequences = defaultdict(list)
    clear_count = 0

    with open(filepath) as file:
        for line in file:
            entry = json.loads(line)
            req = entry.get("request", {})
            res = entry.get("response", {})
            url = req.get("url", "")
            method = req.get("method", "").lower()
            data = req.get("data", {})

            if method == "delete" and "/cart/clear" in url:
                clear_count += 1
                cart.clear()

            elif method == "post" and "/cart/addItem" in url:
                item = data.get("name")
                if item:
                    if cart:
                        item_sequences[cart[-1]].append(item)
                    cart.append(item)
                    item_add_freq[item] += 1
                    cart_snapshots.append(list(cart))

    return {
        "item_add_freq": item_add_freq,
        "total_items": sum(item_add_freq.values()),
        "clear_count": clear_count,
        "final_cart": cart,
        "raw_total": sum(PRICES[item] for item in cart),
        "discounted_total": calculate_discounted_total(cart),
        "cart_snapshots": cart_snapshots,
        "item_sequences": item_sequences,
    }

def generate_pdf_report(results, output_path):
    c = canvas.Canvas(output_path, pagesize=A4)
    width, height = A4
    y = height - inch

    def write_line(text, indent=0, size=12):
        nonlocal y
        c.setFont("Helvetica", size)
        c.drawString(50 + indent, y, text)
        y -= 20

    write_line("🛒 Shopping Cart Activity Report", size=16)
    write_line("")

    write_line("Most Added Items:")
    for item, count in results["item_add_freq"].most_common():
        write_line(f"{item}: {count} times", indent=20)

    write_line("")
    write_line(f"Cart was cleared {results['clear_count']} times")
    write_line("")
    write_line(f"Final Cart: {', '.join(results['final_cart'])}")
    write_line(f"Raw Total (without offers): {results['raw_total']}p")
    write_line(f"Discounted Total (with offers): {results['discounted_total']}p")
    write_line("")

    write_line("Common Sequences (first → next):")
    for item, next_items in results["item_sequences"].items():
        next_item_freq = Counter(next_items).most_common(1)
        if next_item_freq:
            write_line(f"{item} → {next_item_freq[0][0]} ({next_item_freq[0][1]} times)", indent=20)

    c.save()
    print(f"✅ PDF report generated at: {output_path}")

# 🔍 Run full analysis and create PDF
results = analyze_log(LOG_FILE)
generate_pdf_report(results, REPORT_FILE)
