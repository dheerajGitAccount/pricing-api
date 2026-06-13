from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.units import cm
from reportlab.lib import colors
from reportlab.platypus import SimpleDocTemplate, Paragraph, Spacer, Table, TableStyle, HRFlowable
from reportlab.lib.enums import TA_LEFT, TA_CENTER

doc = SimpleDocTemplate(
    "pricing-api-summary.pdf",
    pagesize=A4,
    leftMargin=2*cm, rightMargin=2*cm,
    topMargin=2*cm, bottomMargin=2*cm
)

styles = getSampleStyleSheet()
story  = []

# ── Custom styles ──────────────────────────────────────────────────────────────
title_style = ParagraphStyle("Title2", parent=styles["Title"],
    fontSize=20, textColor=colors.HexColor("#1a3c5e"), spaceAfter=6)

h1_style = ParagraphStyle("H1", parent=styles["Heading1"],
    fontSize=13, textColor=colors.HexColor("#1a3c5e"),
    spaceBefore=14, spaceAfter=4)

h2_style = ParagraphStyle("H2", parent=styles["Heading2"],
    fontSize=11, textColor=colors.HexColor("#2e6da4"),
    spaceBefore=10, spaceAfter=3)

body = ParagraphStyle("Body", parent=styles["Normal"],
    fontSize=9.5, leading=14, spaceAfter=4)

code_style = ParagraphStyle("Code", parent=styles["Normal"],
    fontName="Courier", fontSize=8.5, leading=13,
    backColor=colors.HexColor("#f4f4f4"),
    textColor=colors.HexColor("#2d2d2d"),
    leftIndent=10, rightIndent=10, spaceAfter=6,
    borderPad=6)

bullet = ParagraphStyle("Bullet", parent=body,
    leftIndent=16, bulletIndent=6, spaceAfter=3)

def hr():
    return HRFlowable(width="100%", thickness=0.5,
                      color=colors.HexColor("#cccccc"), spaceAfter=6)

def h1(text):  return Paragraph(text, h1_style)
def h2(text):  return Paragraph(text, h2_style)
def p(text):   return Paragraph(text, body)
def code(text):return Paragraph(text.replace("\n","<br/>").replace(" ","&nbsp;"), code_style)
def bl(text):  return Paragraph(f"• &nbsp;{text}", bullet)
def sp(n=6):   return Spacer(1, n)

# ── Title ──────────────────────────────────────────────────────────────────────
story += [
    Paragraph("Pricing API — Project Summary", title_style),
    p("Spring Boot REST API for managing product prices, price configurations and sales data."),
    hr(), sp()
]

# ── 1. Project Structure ───────────────────────────────────────────────────────
story += [h1("1. Project Structure"), sp(4)]

struct_data = [
    ["Layer", "Class", "Responsibility"],
    ["Model",      "Product",                    "Product entity (id, name, price)"],
    ["Model",      "Sale",                        "Sale entity (product, date, qty, revenue)"],
    ["Model",      "PriceConfiguration",          "Price config entity (product, price, currency, dates)"],
    ["DTO",        "PriceConfigurationRequest",   "JSON request body for price config"],
    ["DTO",        "DailySalesSummary",            "Response for daily sales query"],
    ["DTO",        "OptimumPriceResponse",         "Response for optimum price query"],
    ["Repository", "ProductRepository",            "CRUD for products"],
    ["Repository", "SaleRepository",               "Sales queries — daily & optimum price"],
    ["Repository", "PriceConfigurationRepository", "Overlap query for price configs"],
    ["Service",    "PricingService",               "Product & sales business logic"],
    ["Service",    "PriceConfigurationService",    "Overlap-aware price config upsert"],
    ["Controller", "PricingController",            "Product & sales REST endpoints"],
    ["Controller", "PriceConfigurationController", "Price config REST endpoint"],
]

ts = TableStyle([
    ("BACKGROUND",  (0,0), (-1,0),  colors.HexColor("#1a3c5e")),
    ("TEXTCOLOR",   (0,0), (-1,0),  colors.white),
    ("FONTNAME",    (0,0), (-1,0),  "Helvetica-Bold"),
    ("FONTSIZE",    (0,0), (-1,-1), 8.5),
    ("ROWBACKGROUNDS",(0,1),(-1,-1),[colors.white, colors.HexColor("#eef4fb")]),
    ("GRID",        (0,0), (-1,-1), 0.4, colors.HexColor("#cccccc")),
    ("VALIGN",      (0,0), (-1,-1), "MIDDLE"),
    ("PADDING",     (0,0), (-1,-1), 5),
])
t = Table(struct_data, colWidths=[3.2*cm, 5.5*cm, 8.3*cm])
t.setStyle(ts)
story += [t, sp()]

# ── 2. REST Endpoints ──────────────────────────────────────────────────────────
story += [h1("2. REST Endpoints"), sp(4)]

ep_data = [
    ["Method", "URL", "Description"],
    ["POST",   "/api/products",                          "Create a product"],
    ["GET",    "/api/products",                          "List all products"],
    ["PUT",    "/api/products/{id}/price?price=99.9",    "Update product price"],
    ["POST",   "/api/sales?productId=1&quantity=5\n&saleDate=2025-01-15", "Record a sale"],
    ["GET",    "/api/sales/daily?date=2025-01-15",       "Daily sales summary"],
    ["GET",    "/api/sales/optimum-price",               "Product with highest revenue"],
    ["PUT",    "/api/price-configurations",              "Add or update price config (JSON body)"],
]

method_colors = {
    "POST": colors.HexColor("#2e7d32"),
    "GET":  colors.HexColor("#1565c0"),
    "PUT":  colors.HexColor("#e65100"),
}

ep_ts = TableStyle([
    ("BACKGROUND",  (0,0), (-1,0),  colors.HexColor("#1a3c5e")),
    ("TEXTCOLOR",   (0,0), (-1,0),  colors.white),
    ("FONTNAME",    (0,0), (-1,0),  "Helvetica-Bold"),
    ("FONTNAME",    (0,1), (0,-1),  "Helvetica-Bold"),
    ("FONTSIZE",    (0,0), (-1,-1), 8.5),
    ("ROWBACKGROUNDS",(0,1),(-1,-1),[colors.white, colors.HexColor("#eef4fb")]),
    ("GRID",        (0,0), (-1,-1), 0.4, colors.HexColor("#cccccc")),
    ("VALIGN",      (0,0), (-1,-1), "MIDDLE"),
    ("PADDING",     (0,0), (-1,-1), 5),
])
for i, row in enumerate(ep_data[1:], start=1):
    c = method_colors.get(row[0], colors.black)
    ep_ts.add("TEXTCOLOR", (0,i), (0,i), c)

ep_table = Table(ep_data, colWidths=[1.8*cm, 7.5*cm, 7.7*cm])
ep_table.setStyle(ep_ts)
story += [ep_table, sp()]

# ── 3. PriceConfiguration JSON ────────────────────────────────────────────────
story += [
    h1("3. PriceConfiguration — JSON Request Body"),
    p("Send this JSON body to <b>PUT /api/price-configurations</b>:"),
    sp(4),
    code("""{
  "productId" : 2,
  "price"     : 49.99,
  "currency"  : "GBP",
  "startDate" : "2025-03-01",
  "endDate"   : "2025-03-10"
}"""),
]

# ── 4. Overlap Logic ───────────────────────────────────────────────────────────
story += [
    h1("4. Overlap Handling Logic"),
    p("When a new price configuration is submitted, any existing records for the same product "
      "that overlap the new date range are automatically adjusted. No request is ever rejected."),
    sp(4),
]

cases = [
    ("Case 1 — Existing fully covered by new", "DELETE existing",
     "Existing:  |--Mar03---Mar07--|\nNew:       |--Mar01-----------Mar10--|\nResult:    |--Mar01-----------Mar10--|"),
    ("Case 2 — New fully inside existing", "SPLIT existing into two",
     "Existing:  |--Mar01-----------Mar10--|\nNew:            |--Mar04---Mar07--|\nResult:    |--Mar01-Mar03--|  |--Mar04---Mar07--|  |--Mar08-Mar10--|"),
    ("Case 3 — Existing overlaps on the right", "TRIM existing start",
     "Existing:       |--Mar05-------Mar15--|\nNew:       |--Mar01---Mar08--|\nResult:    |--Mar01---Mar08--|  |--Mar09-Mar15--|"),
    ("Case 4 — Existing overlaps on the left", "TRIM existing end",
     "Existing:  |--Mar01---Mar08--|\nNew:              |--Mar05-------Mar15--|\nResult:    |--Mar01-Mar04--|  |--Mar05-------Mar15--|"),
]

for title, action, diagram in cases:
    story += [
        h2(title),
        p(f"Action: <b>{action}</b>"),
        code(diagram),
    ]

# ── 5. Transaction ─────────────────────────────────────────────────────────────
story += [
    h1("5. Transaction & Atomicity"),
    p("The <b>addOrUpdate</b> method is annotated with:"),
    code("@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)"),
    bl("<b>SERIALIZABLE isolation</b> — prevents concurrent transactions from inserting "
       "overlapping records between the read and write phases."),
    bl("<b>rollbackFor = Exception.class</b> — any failure (delete or insert) rolls back "
       "the entire operation, leaving the database in its original state."),
    bl("<b>toDelete list</b> — all existing records to remove are batched and executed first "
       "via deleteAll()."),
    bl("<b>toAdd list</b> — all trimmed/split records plus the new config are batched and "
       "executed via saveAll(). The new config is always last in the list."),
    sp()
]

# ── 6. Build & Run ─────────────────────────────────────────────────────────────
story += [
    h1("6. Build & Run"),
    code("./gradlew clean bootRun"),
    p("H2 Console: <b>http://localhost:8080/h2-console</b>"),
    sp(3),
]

conn_data = [
    ["Field",        "Value"],
    ["JDBC URL",     "jdbc:h2:mem:pricingdb"],
    ["User Name",    "sa"],
    ["Password",     "(leave blank)"],
]
conn_ts = TableStyle([
    ("BACKGROUND",  (0,0), (-1,0),  colors.HexColor("#1a3c5e")),
    ("TEXTCOLOR",   (0,0), (-1,0),  colors.white),
    ("FONTNAME",    (0,0), (-1,0),  "Helvetica-Bold"),
    ("FONTSIZE",    (0,0), (-1,-1), 8.5),
    ("ROWBACKGROUNDS",(0,1),(-1,-1),[colors.white, colors.HexColor("#eef4fb")]),
    ("GRID",        (0,0), (-1,-1), 0.4, colors.HexColor("#cccccc")),
    ("PADDING",     (0,0), (-1,-1), 5),
])
conn_table = Table(conn_data, colWidths=[4*cm, 9*cm])
conn_table.setStyle(conn_ts)
story += [conn_table]

doc.build(story)
print("PDF generated: pricing-api-summary.pdf")
