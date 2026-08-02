import yaml, json, os, re

BASE = "/srv/agronomia/frontend/src/content"

def parse_md(path):
    with open(path, encoding="utf-8") as f:
        text = f.read()
    m = re.match(r"^---\n(.*?)\n---\n(.*)$", text, re.DOTALL)
    front = yaml.safe_load(m.group(1))
    body = m.group(2).strip()
    return front, body

def strip_strings(obj):
    if isinstance(obj, str):
        return obj.strip()
    if isinstance(obj, list):
        return [strip_strings(x) for x in obj]
    if isinstance(obj, dict):
        return {k: strip_strings(v) for k, v in obj.items()}
    return obj

species = []
for fname in sorted(os.listdir(f"{BASE}/species")):
    if not fname.endswith(".md"):
        continue
    slug = fname[:-3]
    front, body = parse_md(f"{BASE}/species/{fname}")
    front["slug"] = slug
    front["descripcion"] = body.strip()
    species.append(strip_strings(front))

# La coleccion etnobotanicacont se elimino del sitio (commit 76cb87d): la seccion de
# Etnobotanica ahora se deriva directo de "species" (campo especie.etnobotanica + estado
# ACTIVO), asi que ya no hay una carpeta separada que extraer aqui.

blog = []
for fname in sorted(os.listdir(f"{BASE}/blog")):
    if not fname.endswith(".md"):
        continue
    slug = fname[:-3]
    front, body = parse_md(f"{BASE}/blog/{fname}")
    front["slug"] = slug
    front["contenido"] = body
    if "pubDate" in front:
        front["pubDate"] = str(front["pubDate"])
    blog.append(front)

blog = [strip_strings(b) for b in blog]

out = {"species": species, "blog": blog}
outpath = "/home/admininfra/EcotecFloraMedicaApp/app/src/main/assets/content.json"
with open(outpath, "w", encoding="utf-8") as f:
    json.dump(out, f, ensure_ascii=False, indent=2)

print(f"species: {len(species)}, blog: {len(blog)}")
print(f"written to {outpath}")
import subprocess
print(subprocess.run(["du", "-h", outpath], capture_output=True, text=True).stdout)
